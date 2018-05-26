package org.sysu.renCommon.config;

/**
 * Author: Rinkako
 * Date  : 2018/5/27
 * Usage : Interface for config service event listening.
 */
public interface IConfigListener {

    void HandleAdd(String key, String value);

    void HandleUpdate(String key, String value);

    void HandleDelete(String key, String value);
}
