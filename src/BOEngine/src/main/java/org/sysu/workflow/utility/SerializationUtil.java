/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.workflow.BOInstance;
import org.sysu.workflow.model.SCXML;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
    public static String JsonSerialization(Object serializable, String rtid) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(serializable);
        }
        catch (Exception ex) {
            LogUtil.Log("When json serialization exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, rtid);
            return null;
        }
    }

    /**
     * Un jsonify an object.
     * @param serialized json serialized string
     * @param type return class type
     * @return class instance of type
     */
    public static <Ty> Ty JsonDeserialization(String serialized, Class<Ty> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(serialized, type);
        }
        catch (Exception ex) {
            LogUtil.Log("When un serialization exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Serialize SCXML instance to a string.
     * @param scxml {@code SCXML} instance
     * @return serialized string
     */
    public static byte[] SerializationSCXMLToByteArray(SCXML scxml) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(scxml);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            LogUtil.Log("When SerializationSCXMLToString exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Deserialize string to SCXML instance.
     * @param serializedSCXML string to be deserialized
     * @return {@code SCXML} instance.
     */
    public static SCXML DeserializationSCXMLByByteArray(byte[] serializedSCXML) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedSCXML);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (SCXML) objectInputStream.readObject();
        }
        catch (Exception ex) {
            LogUtil.Log("When DeSerializationSCXML exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Serialize BO instance to a string.
     * @param instance {@code BOInstance} instance
     * @return serialized string
     */
    public static byte[] SerializationBOInstanceToByteArray(BOInstance instance) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(instance);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            LogUtil.Log("When SerializationBOInstanceToString exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }

    /**
     * Deserialize string to BO instance.
     * @param serialized string to be deserialized
     * @return {@code SCXML} instance.
     */
    public static BOInstance DeserializationBOInstanceByByteArray(byte[] serialized) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialized);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (BOInstance) objectInputStream.readObject();
        }
        catch (Exception ex) {
            LogUtil.Log("When DeSerializationBOInstance exception occurred, " + ex.toString(),
                    SerializationUtil.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }
}
