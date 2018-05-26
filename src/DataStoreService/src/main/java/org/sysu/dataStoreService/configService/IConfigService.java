package org.sysu.dataStoreService.configService;

import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/5/26
 * Usage : Interface for config store operations.
 */
public interface IConfigService {

    /**
     * Add a config to the store.
     *
     * @param service service name
     * @param key     config key in String
     * @param value   config value in String
     */
    void Add(String service, String key, String value) throws Exception;

    /**
     * Update a config in the store.
     *
     * @param service service name
     * @param key     config key in String
     * @param value   config value in String
     */
    void Update(String service, String key, String value) throws Exception;

    /**
     * Remove a config from the store.
     *
     * @param service service name
     * @param key     config key
     */
    void Delete(String service, String key) throws Exception;

    /**
     * Retrieve a config value from the store by its key.
     *
     * @param service service name
     * @param key     config key in String
     * @return value in String
     */
    String Retrieve(String service, String key) throws Exception;

    /**
     * Retrieve all config.
     *
     * @param service service name
     * @return Map of key-value in String
     */
    Map<String, String> RetrieveAll(String service) throws Exception;
}