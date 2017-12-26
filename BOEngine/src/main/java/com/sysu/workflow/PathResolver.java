
package com.sysu.workflow;

/**
 * Interface for a component that may be used by the SCXML engines
 * to resolve context sensitive paths.
 * SCXML引擎使用的路径解析组件的借口，
 * 解析上下文的相对路径
 */
public interface PathResolver {

    /**
     * Resolve this context sensitive path to an absolute URL.
     * 将上下文中相对敏感的路径解析成一个绝对的URL
     *
     * @param ctxPath Context sensitive path, can be a relative URL
     * @return Resolved path (an absolute URL) or <code>null</code>
     */
    String resolvePath(String ctxPath);

    /**
     * Get a PathResolver rooted at this context sensitive path.
     * 返回PathResolver
     *
     * @param ctxPath Context sensitive path, can be a relative URL
     * @return Returns a new resolver rooted at ctxPath
     */
    PathResolver getResolver(String ctxPath);

}

