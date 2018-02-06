/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Some useful helper methods.
 */
public class CommonUtil {
    /**
     * Check if a string is null or empty.
     * @param str string to be checked
     * @return boolean of result
     */
    public static boolean IsNullOrEmpty(String str) {
        return str != null && !str.equals("");
    }

    /**
     * Zip a key vector and a value vector to a hash map.
     * Key list and value list should have same size.
     * @param keyList key vector, all entry should be unique
     * @param valueList value vector
     * @param <TyK> Key type
     * @param <TyV> Value type
     * @return A HashMap for (key, value)
     */
    public static <TyK, TyV> HashMap<TyK, TyV> ZipVector(List<TyK> keyList, List<TyV> valueList) {
        if (keyList == null || valueList == null) {
            return null;
        }
        assert keyList.size() == valueList.size();
        HashMap<TyK, TyV> retMap = new HashMap<>();
        for (int i = 0; i < keyList.size(); i++) {
            retMap.put(keyList.get(i), valueList.get(i));
        }
        return retMap;
    }
}
