/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.allocator.RAllocator;
import org.sysu.renResourcing.filter.RFilter;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Some static methods for reflection.
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
            Class classType = Class.forName(ReflectUtil.ALLOCATOR_PACKAGE_PATH + allocatorName + ReflectUtil.ALLOCATOR_POSTFIX);
            return (RAllocator) classType.newInstance();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Request %s, Reflect create allocator (%s) failed, %s", rstid, allocatorName, ex),
                    ReflectUtil.class.getName(), LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to engine
        }
    }

    /**
     * Create a new filter by its name.
     * @param filterName name of filter to be created
     * @param rstid rs request id
     * @param rtid process rtid
     * @return Specific filter
     * @throws Exception reflect instance create failed
     */
    public static RFilter ReflectFilter(String filterName, String rstid, String rtid) throws Exception {
        try {
            Class classType = Class.forName(ReflectUtil.FILTER_PACKAGE_PATH + filterName + ReflectUtil.FILTER_POSTFIX);
            return (RFilter) classType.newInstance();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Request %s, Reflect create filter (%s) failed, %s", rstid, filterName, ex),
                    ReflectUtil.class.getName(), LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to engine
        }
    }

    /**
     * Package path of Allocators.
     */
    private static final String ALLOCATOR_PACKAGE_PATH = "org.sysu.renResourcing.allocator.";

    /**
     * Package path of Allocators.
     */
    private static final String FILTER_PACKAGE_PATH = "org.sysu.renResourcing.filter.";

    /**
     * Postfix of Allocator.
     */
    private static final String ALLOCATOR_POSTFIX = "Allocator";

    /**
     * Postfix of Filter.
     */
    private static final String FILTER_POSTFIX = "Filter";
}
