/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.consistency;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/4/6
 * Usage : Cluster management by ZooKeeper.
 */
public class ZKCluster implements IClusterManager {

    /**
     * Get the global instance of ZKCluster manager.
     * This method is thread SAFE.
     * @return ZKCluster instance.
     */
    public static ZKCluster GetInstance() throws Exception {
        if (ZKCluster.syncObj == null) {
            synchronized (ZKCluster.class) {
                if (ZKCluster.syncObj == null) {
                    return ZKCluster.syncObj = new ZKCluster();
                }
            }
        }
        return ZKCluster.syncObj;
    }

    /**
     * Write a string data to a cluster global data node.
     *
     * @param path    global data path
     * @param content content of node
     */
    @Override
    public void WriteNodeByString(String path, String content) throws Exception {
        this.WriteNode(this.NormalizePath(path), content.getBytes());
    }

    /**
     * Read a string data from a cluster global data node.
     *
     * @param path global data path
     * @return content of node
     */
    @Override
    public String ReadNodeByString(String path) throws Exception {
        return new String(this.ReadNode(this.NormalizePath(path)));
    }

    /**
     * Write data to a cluster global data node.
     *
     * @param path    global data path
     * @param content content of node
     */
    @Override
    public void WriteNode(String path, byte[] content) throws Exception {
        String nPath = this.NormalizePath(path);
        this.ZClient.checkExists().creatingParentContainersIfNeeded().forPath(nPath);
        this.ZClient.setData().forPath(nPath, content);
    }

    /**
     * Read data from a cluster global data node.
     *
     * @param path global data path
     * @return content of node
     */
    @Override
    public byte[] ReadNode(String path) throws Exception {
        return this.ZClient.getData().forPath(this.NormalizePath(path));
    }

    /**
     * Get all children path of a specific data node.
     *
     * @param path global data path
     * @return a List of string of children path
     */
    @Override
    public List<String> GetChildren(String path) throws Exception {
        return this.ZClient.getChildren().forPath(this.NormalizePath(path));
    }

    /**
     * Remove a data node from cluster.
     *
     * @param path global data path
     */
    @Override
    public void RemoveNode(String path) throws Exception {
        this.ZClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(this.NormalizePath(path));
    }

    /**
     * Check if a data node exists in cluster.
     *
     * @param path global data path
     * @return true if node exist
     */
    @Override
    public boolean Contains(String path) throws Exception {
        return this.ZClient.checkExists().forPath(this.NormalizePath(path)) != null;
    }

    /**
     * WriteLock a global node path.
     *
     * @param path global data path
     */
    @Override
    public synchronized void WriteLock(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessReadWriteLock lock = ZKCluster.ZLockTable.get(nPath);
        if (lock == null) {
            lock = new InterProcessReadWriteLock(this.ZClient, nPath);
            ZKCluster.ZLockTable.put(nPath, lock);
        }
        lock.writeLock().acquire();
    }

    /**
     * Try to lock a global node path.
     *
     * @param path global data path
     * @return true if get locked by this method
     */
    @Override
    public synchronized boolean TryWriteLock(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessReadWriteLock lock = ZKCluster.ZLockTable.get(nPath);
        if (lock == null) {
            lock = new InterProcessReadWriteLock(this.ZClient, nPath);
            ZKCluster.ZLockTable.put(nPath, lock);
            lock.writeLock().acquire();
            return true;
        }
        return lock.writeLock().isOwnedByCurrentThread();
    }

    /**
     * WriteUnlock a global node path.
     *
     * @param path global data path
     */
    @Override
    public synchronized void WriteUnlock(String path) throws Exception {
        InterProcessReadWriteLock lock = ZKCluster.ZLockTable.get(this.NormalizePath(path));
        if (lock == null) {
            return;
        }
        lock.writeLock().release();
    }

    /**
     * ReadLock a global node path.
     *
     * @param path global data path
     */
    @Override
    public void ReadLock(String path) throws Exception {
        String nPath = this.NormalizePath(path);
        InterProcessReadWriteLock lock = ZKCluster.ZLockTable.get(nPath);
        if (lock == null) {
            lock = new InterProcessReadWriteLock(this.ZClient, nPath);
            ZKCluster.ZLockTable.put(nPath, lock);
        }
        lock.readLock().acquire();
    }

    /**
     * ReadUnlock a global node path.
     *
     * @param path global data path
     */
    @Override
    public void ReadUnlock(String path) throws Exception {
        InterProcessReadWriteLock lock = ZKCluster.ZLockTable.get(this.NormalizePath(path));
        if (lock == null) {
            return;
        }
        lock.readLock().release();
    }

    /**
     * Normalize an URI to global URL.
     * @param uri uri of path
     * @return url of path
     */
    private String NormalizePath(String uri) {
        return String.format("/%s/%s", ZKCluster.NameSpace, uri);
    }

    /**
     * Private constructor for preventing created outside.
     */
    private ZKCluster() throws Exception {
        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);
        Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ZKCluster.ConnectZKAddress)
                .connectionTimeoutMs(ZKCluster.TimeOut)
                .sessionTimeoutMs(ZKCluster.TimeOut)
                .retryPolicy(rp)
                .namespace(ZKCluster.NameSpace);
        this.ZClient = builder.build();
        this.ZClient.start();
        this.ZClient.create().creatingParentContainersIfNeeded().forPath("/" + ZKCluster.NameSpace);
    }

    /**
     * ZooKeeper Client.
     */
    private CuratorFramework ZClient;

    /**
     * Cluster lock table.
     */
    private static final HashMap<String, InterProcessReadWriteLock> ZLockTable = new HashMap<>();

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
    private static ZKCluster syncObj = null;
}
