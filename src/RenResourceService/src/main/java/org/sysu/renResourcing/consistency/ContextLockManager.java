/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.consistency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : This class is a lock manager for all running processes resources
 *         contexts using ReentrantReadWriteLock.
 *         This manager is responsible for controlling context concurrency
 *         access for this ONE Resource Service. NOTICE that it has no idea
 *         about this context is used by other RS instance or not. For the
 *         consistency, we appoint: 1. Participants and their work queues
 *         MUST NOT be locked since it is probably accessing by another RS.
 *         2. Workitem can use lock, since we make sure that one workitem
 *         ONLY managed by ONE specific RS.
 */
public class ContextLockManager {

    /**
     * WriteLock table.
     */
    private static ConcurrentHashMap<String, ReentrantReadWriteLock> lockTable = new ConcurrentHashMap<>();

    /**
     * <p>Acquires the read lock if the write lock is not held by
     * another thread and returns immediately.
     *
     * <p>If the write lock is held by another thread then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until the read lock has been acquired.
     *
     * @param clazz context class
     * @param contextId context id
     */
    public static void ReadLock(Class<?> clazz, String contextId) {
        String lockId = ContextLockManager.GenerateLockKey(clazz, contextId);
        ContextLockManager.CheckEmptyLock(lockId);
        ContextLockManager.lockTable.get(lockId).readLock().lock();
    }

    /**
     * Acquires the write lock.
     *
     * <p>Acquires the write lock if neither the read nor write lock
     * are held by another thread
     * and returns immediately, setting the write lock hold count to
     * one.
     *
     * <p>If the current thread already holds the write lock then the
     * hold count is incremented by one and the method returns
     * immediately.
     *
     * <p>If the lock is held by another thread then the current
     * thread becomes disabled for thread scheduling purposes and
     * lies dormant until the write lock has been acquired, at which
     * time the write lock hold count is set to one.
     *
     * @param clazz context class
     * @param contextId context id
     */
    public static void WriteLock(Class<?> clazz, String contextId) {
        String lockId = ContextLockManager.GenerateLockKey(clazz, contextId);
        ContextLockManager.CheckEmptyLock(lockId);
        ContextLockManager.lockTable.get(lockId).writeLock().lock();
    }

    /**
     * Acquires the write lock only if it is not held by another thread
     * at the time of invocation.
     *
     * <p>If the current thread already holds this lock then the
     * hold count is incremented by one and the method returns
     * {@code true}.
     *
     * <p>If the lock is held by another thread then this method
     * will return immediately with the value {@code false}.
     *
     * @param clazz context class
     * @param contextId context id
     * @return {@code true} if the lock was free and was acquired
     * by the current thread, or the write lock was already held
     * by the current thread; and {@code false} otherwise.
     */
    public static boolean TryWriteLock(Class<?> clazz, String contextId) {
        String lockId = ContextLockManager.GenerateLockKey(clazz, contextId);
        ContextLockManager.CheckEmptyLock(lockId);
        return ContextLockManager.lockTable.get(lockId).writeLock().tryLock();
    }

    /**
     * WriteUnlock a read lock.
     * @param clazz context class
     * @param contextId context id
     */
    public static void ReadUnLock(Class<?> clazz, String contextId) {
        String lockId = ContextLockManager.GenerateLockKey(clazz, contextId);
        ReentrantReadWriteLock lock = ContextLockManager.lockTable.get(lockId);
        if (lock != null) {
            lock.readLock().unlock();
        }
    }

    /**
     * WriteUnlock a write lock.
     * @param clazz context class
     * @param contextId context id
     */
    public static void WriteUnLock(Class<?> clazz, String contextId) {
        String lockId = ContextLockManager.GenerateLockKey(clazz, contextId);
        ReentrantReadWriteLock lock = ContextLockManager.lockTable.get(lockId);
        if (lock != null) {
            lock.writeLock().unlock();
        }
    }

    /**
     * Generate a lock for a context.
     * NOTICE this method is global synchronized, since concurrent create lock is invalid.
     * @param keyId lock key id
     */
    private synchronized static void CheckEmptyLock(String keyId) {
        if (!ContextLockManager.lockTable.containsKey(keyId)) {
            ContextLockManager.lockTable.put(keyId, new ReentrantReadWriteLock());
        }
    }

    /**
     * Get a key string for a context id.
     * @param originalId context id
     * @return lock key id
     */
    private static String GenerateLockKey(Class<?> clazz, String originalId) {
        return String.format("%s@@%s", clazz.getSimpleName(), originalId);
    }
}
