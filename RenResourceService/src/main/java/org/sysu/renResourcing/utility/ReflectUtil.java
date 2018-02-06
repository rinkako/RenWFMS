/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

import org.sysu.renResourcing.allocator.RAllocator;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Some static methods for reflect.
 */
public final class ReflectUtil {
    /**
     * Create a new allocator by its name.
     * @param allocatorName name of allocator to be created
     * @param rstid rs request id
     * @param rtid process rtid
     * @return Specific allocator
     * @throws Exception reflect instance create failed
     */
    public static RAllocator ReflectAllocator(String allocatorName, String rstid, String rtid) throws Exception {
        try {
            Class classType = Class.forName(ReflectUtil.ALLOCATOR_PACKAGE_PATH + allocatorName);
            return (RAllocator) classType.newInstance();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Request %s, Reflect create allocator (%s) failed, %s", rstid, allocatorName, ex),
                    ReflectUtil.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to engine
        }
    }

    /**
     * Package path of Allocators.
     */
    private static final String ALLOCATOR_PACKAGE_PATH = "allocator.";
}
