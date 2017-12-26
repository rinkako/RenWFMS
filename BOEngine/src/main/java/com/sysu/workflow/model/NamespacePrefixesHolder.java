
package com.sysu.workflow.model;

import java.util.Map;

/**
 *
 * 名称前缀持有器，保存了名称前缀信息，为了Xpath 表达式求值使用
 */
public interface NamespacePrefixesHolder {

    /**
     * 设置名称前缀持有器
     *
     * @param namespaces The namespaces prefix map.
     */
    void setNamespaces(Map<String, String> namespaces);

    /**
     * 得到名称前缀持有器
     *
     * @return The namespaces prefix map.
     */
    Map<String, String> getNamespaces();

}

