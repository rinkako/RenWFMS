/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.context;

import org.sysu.renCommon.utility.TimestampUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Data Package of observable notification message.
 */
public final class ObservableMessage implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Notification code.
     */
    private String code;

    /**
     * Payload dictionary.
     */
    private HashMap<String, Object> retDict = new HashMap<>();

    /**
     * Notification timestamp.
     */
    private Timestamp timestamp = TimestampUtil.GetCurrentTimestamp();

    /**
     * Create a new observable notification message.
     * @param code notification code string
     */
    public ObservableMessage(String code) {
        this.code = code;
    }

    /**
     * Add notification payload.
     * @param key payload fetching key
     * @param value payload object
     */
    public void AddPayload(String key, Object value) {
        this.retDict.put(key, value);
    }

    /**
     * Get notification code.
     * @return code string defined in {@code GlobalContext}
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Get payload dictionary.
     * @return Hash table contains payloads
     */
    public HashMap<String, Object> getRetDict() {
        return this.retDict;
    }

    /**
     * Get notification created timestamp.
     * @return this message constructed timestamp
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
}
