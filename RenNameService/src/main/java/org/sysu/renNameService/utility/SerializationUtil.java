/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sysu.renNameService.roleMapping.RoleMappingService;

/**
 * Author: Rinkako
 * Date  : 2018/1/22
 * Usage :
 */
public class SerializationUtil {
    /**
     * Jsonify an object.
     * @param serializable object to be converted to json
     * @return json string
     */
    public static String JsonSerilization(Object serializable, String rtid) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(serializable);
        }
        catch (Exception ex) {
            LogUtil.Log("When json serialization exception occurred, " + ex.toString(),
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            return null;
        }
    }
}
