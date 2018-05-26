package org.sysu.renCommon.config;

import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/5/26
 * Usage : Interface for slave services retrieving configurations from config store.
 */
public interface IConfigSlave {

    /**
     * Config store location.
     */
    String Namespace = "Configuration";

    /**
     * Initialize config store.
     */
    void Init();

    /**
     * Reload config from store and apply to this instance.
     */
    void Reload();

    /**
     * Retrieve a config value from the store by its key.
     *
     * @param key config key in String
     * @return value in String
     */
    String Retrieve(String key);

    /**
     * Retrieve all config.
     *
     * @return Map of key-value in String
     */
    Map<String, String> RetrieveAll();
}
