package org.sysu.dataStoreService.configService;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author: Rinkako
 * Date  : 2018/5/26
 * Usage : Config service under ZooKeeper.
 */
public class ZKConfigService implements IConfigService {

    /**
     * Get the global instance of ZKConfigService manager.
     * This method is thread SAFE.
     * @return ZKConfigService instance.
     */
    public static ZKConfigService GetInstance() throws Exception {
        if (ZKConfigService.syncObj == null) {
            synchronized (ZKConfigService.class) {
                if (ZKConfigService.syncObj == null) {
                    return ZKConfigService.syncObj = new ZKConfigService();
                }
            }
        }
        return ZKConfigService.syncObj;
    }

    /**
     * Add a config to the store.
     *
     * @param service service name
     * @param key     config key in String
     * @param value   config value in String
     */
    @Override
    public void Add(String service, String key, String value) throws Exception {
        this.WriteNodeByString(String.format("%s/%s", service, key), value);
    }

    /**
     * Update a config in the store.
     *
     * @param service service name
     * @param key     config key in String
     * @param value   config value in String
     */
    @Override
    public void Update(String service, String key, String value) throws Exception {
        this.WriteNodeByString(String.format("%s/%s", service, key), value);
    }

    /**
     * Remove a config from the store.
     *
     * @param service service name
     * @param key     config key
     */
    @Override
    public void Delete(String service, String key) throws Exception {
        this.RemoveNode(String.format("%s/%s", service, key));
    }

    /**
     * Retrieve a config value from the store by its key.
     *
     * @param service service name
     * @param key     config key in String
     * @return value in String
     */
    @Override
    public String Retrieve(String service, String key) throws Exception {
        return this.ReadNodeByString(String.format("%s/%s", service, key));
    }

    /**
     * Retrieve all config.
     *
     * @param service service name
     * @return Map of key-value in String
     */
    @Override
    public Map<String, String> RetrieveAll(String service) throws Exception {
        List<String> children = this.GetChildren(service);
        Map<String, String> retMap = new HashMap<>();
        for (String child : children) {
            String childValue = this.ReadNodeByString(child);
            String[] childKeyItem = child.split("/");
            retMap.put(childKeyItem[childKeyItem.length - 1], childValue);
        }
        return retMap;
    }

    /**
     * Write a string data to a cluster global data node.
     *
     * @param path    global data path
     * @param content content of node
     */
    private void WriteNodeByString(String path, String content) throws Exception {
        this.WriteNode(path, content.getBytes());
    }

    /**
     * Read a string data from a cluster global data node.
     *
     * @param path global data path
     * @return content of node
     */
    private String ReadNodeByString(String path) throws Exception {
        return new String(this.ReadNode(path));
    }

    /**
     * Write data to a cluster global data node.
     *
     * @param path    global data path
     * @param content content of node
     */
    private void WriteNode(String path, byte[] content) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.ZClient, nPath);
        try {
            mutex.acquire();
            this.ZClient.checkExists().creatingParentContainersIfNeeded().forPath(nPath);
            this.ZClient.create().orSetData().withMode(CreateMode.PERSISTENT).forPath(nPath, content);
        }
        finally {
            mutex.release();
        }
    }

    /**
     * Read data from a cluster global data node.
     *
     * @param path global data path
     * @return content of node
     */
    private byte[] ReadNode(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.ZClient, nPath);
        try {
            mutex.acquire();
            return this.ZClient.getData().forPath(nPath);
        }
        finally {
            mutex.release();
        }
    }

    /**
     * Get all children path of a specific data node.
     *
     * @param path global data path
     * @return a List of string of children path
     */
    public List<String> GetChildren(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.ZClient, nPath);
        try {
            mutex.acquire();
            return this.ZClient.getChildren().forPath(nPath);
        }
        finally {
            mutex.release();
        }
    }

    /**
     * Remove a data node from cluster.
     *
     * @param path global data path
     */
    private void RemoveNode(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.ZClient, nPath);
        try {
            mutex.acquire();
            this.ZClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(nPath);
        }
        finally {
            mutex.release();
        }
    }

    /**
     * Check if a data node exists in cluster.
     *
     * @param path global data path
     * @return true if node exist
     */
    private boolean Contains(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessMutex mutex = new InterProcessMutex(this.ZClient, nPath);
        try {
            mutex.acquire();
            return this.ZClient.checkExists().forPath(nPath) != null;
        }
        finally {
            mutex.release();
        }
    }

    /**
     * Normalize an URI to global URL.
     * @param uri uri of path
     * @return url of path
     */
    private String NormalizePath(String uri) {
        return String.format("/%s/%s", ZKConfigService.NameSpace, uri);
    }

    /**
     * Private constructor for preventing created outside.
     */
    private ZKConfigService() throws Exception {
        RetryPolicy rp = new RetryNTimes(10, 1000);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ZKConfigService.ConnectZKAddress)
                .connectionTimeoutMs(ZKConfigService.TimeOut)
                .sessionTimeoutMs(ZKConfigService.TimeOut)
                .retryPolicy(rp);
        this.ZClient = builder.build();
        this.ZClient.start();
    }

    /**
     * ZooKeeper Client.
     */
    private CuratorFramework ZClient;

    /**
     * Configuration namespace.
     */
    private static final String NameSpace = "Configuration";

    /**
     * [Configurable]
     * ZooKeeper server address. split by `,`.
     */
    private static final String ConnectZKAddress = "127.0.0.1:2181";

    /**
     * [Configurable]
     * ZooKeeper timeout.
     */
    private static final Integer TimeOut = 5000;

    /**
     * Global static instance.
     */
    private static ZKConfigService syncObj = null;
}