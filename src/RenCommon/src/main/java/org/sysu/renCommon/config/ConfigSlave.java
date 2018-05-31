package org.sysu.renCommon.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Rinkako
 * Date  : 2018/5/27
 * Usage : Methods for slave services retrieving configurations from config store.
 */
public class ConfigSlave implements IConfigSlave {

    /**
     * Get the global instance of ZKConfigService manager.
     * This method is thread SAFE.
     * @return ZKConfigService instance.
     */
    public static ConfigSlave GetInstance() throws Exception {
        if (ConfigSlave.syncObj == null) {
            synchronized (ConfigSlave.class) {
                if (ConfigSlave.syncObj == null) {
                    return ConfigSlave.syncObj = new ConfigSlave();
                }
            }
        }
        return ConfigSlave.syncObj;
    }

    /**
     * Initialize config store.
     */
    @Override
    public void Init() {

    }

    /**
     * Reload config from store and apply to this instance.
     */
    @Override
    public void Reload() {

    }

    /**
     * Retrieve a config value from the store by its key.
     *
     * @param key config key in String
     * @return value in String
     */
    @Override
    public String Retrieve(String key) {
        return null;
    }

    /**
     * Retrieve all config.
     *
     * @return Map of key-value in String
     */
    @Override
    public Map<String, String> RetrieveAll() {
        return null;
    }

    /**
     * Set configuration event listener.
     *
     * @param serviceName current service name
     * @param listener IConfigListener instance
     */
    private void SetListenter(String serviceName, IConfigListener listener) throws Exception {
        TreeCache treeCache = new TreeCache(this.ZClient, "/" + serviceName);
        treeCache.getListenable().addListener((client, event) -> {
            ChildData data = event.getData();
            if (data != null) {
                switch (event.getType()) {
                    case NODE_ADDED:
                        String[] addKeyItem = data.getPath().split("/");
                        listener.HandleAdd(addKeyItem[addKeyItem.length - 1], new String(data.getData()));
                        break;
                    case NODE_REMOVED:
                        String[] removeKeyItem = data.getPath().split("/");
                        listener.HandleDelete(removeKeyItem[removeKeyItem.length - 1], new String(data.getData()));
                        break;
                    case NODE_UPDATED:
                        String[] updateKeyItem = data.getPath().split("/");
                        listener.HandleUpdate(updateKeyItem[updateKeyItem.length - 1], new String(data.getData()));
                        break;
                    default:
                        break;
                }
            } else {
                System.out.println("ignore null data for: " + event.getType());
            }
        });
        treeCache.start();
    }

    /**
     * Private constructor prevents from constructing outside.
     */
    private ConfigSlave() {
        RetryPolicy rp = new RetryNTimes(10, 1000);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ConfigSlave.ConnectZKAddress)
                .connectionTimeoutMs(ConfigSlave.TimeOut)
                .sessionTimeoutMs(ConfigSlave.TimeOut)
                .retryPolicy(rp);
        this.ZClient = builder.build();
        this.ZClient.start();
    }

    /**
     * ZooKeeper Client.
     */
    private CuratorFramework ZClient;

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
     * Singleton object.
     */
    private static ConfigSlave syncObj = null;
}
