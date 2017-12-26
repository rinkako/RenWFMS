package org.sysu.renResourcing.util;

import org.sysu.renResourcing.GlobalDataContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Author: Rinkako
 * Date  : 2017/11/20
 * Usage : This utility used for XML data warping
 */
public class XMLUtil {
    public static String WarpKVP(String key, String value) {
        if (!(XMLUtil.CheckIdentifier(key) && XMLUtil.CheckIdentifier(value))) {
            GlobalDataContext.EngineLogger.Echo("Warping String invalid and will be ignored");
            return "";
        }
        return String.format("<%s>%s</%s>", key, value, key);
    }

    public static boolean CheckIdentifier(String id) {
        throw new NotImplementedException();
    }

}
