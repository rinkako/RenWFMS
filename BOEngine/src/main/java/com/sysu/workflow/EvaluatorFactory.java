
package com.sysu.workflow;

import com.sysu.workflow.env.groovy.GroovyEvaluator;
import com.sysu.workflow.env.javascript.JSEvaluator;
import com.sysu.workflow.env.jexl.JexlEvaluator;
import com.sysu.workflow.env.minimal.MinimalEvaluator;
import com.sysu.workflow.env.xpath.XPathEvaluator;
import com.sysu.workflow.model.ModelException;
import com.sysu.workflow.model.SCXML;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A static singleton factory for {@link EvaluatorProvider}s by supported SCXML datamodel type.
 * 一个静态的单例工厂，evaluatorProvider 提供支持的数据模型
 * <p>
 * The EvaluatorFactory is used to automatically create an {@link Evaluator} instance for an SCXML
 * statemachine when none has been pre-defined and configured for the {@link SCXMLExecutor}.
 * <p/>
 * 当SCXMLExecutor事先没有预定义或者配置过求值器，求值工厂被用来对某一个SCXML状态机自动的创建一个求值器实例，
 * </p>
 * <p>
 * The builtin supported providers are:
 * </p>
 * <ul>
 * <li>no or empty datamodel (default) or datamodel="jexl": {@link JexlEvaluator.JexlEvaluatorProvider}</li>
 * <li>datamodel="ecmascript": {@link JSEvaluator.JSEvaluatorProvider}</li>
 * <li>datamodel="groovy": {@link GroovyEvaluator.GroovyEvaluatorProvider}</li>
 * <li>datamodel="xpath": {@link XPathEvaluator.XPathEvaluatorProvider}</li>
 * <li>datamodel="null": {@link MinimalEvaluator.MinimalEvaluatorProvider}</li>
 * </ul>
 * <p>
 * 扩展求值器
 * For adding additional or overriding the builtin Evaluator implementations use
 * {@link #registerEvaluatorProvider(EvaluatorProvider)} or {@link #unregisterEvaluatorProvider(String)}.
 * </p>
 * <p>
 * 默认的提供者能够被重载使用，setDefaultProvider，
 * The default provider can be overridden using the {@link #setDefaultProvider(EvaluatorProvider)} which will
 * register the provider under the {@link Evaluator#DEFAULT_DATA_MODEL} ("") value for the datamodel.<br>
 * Note: this is <em>not</em> the same as datamodel="null"!
 * </p>
 */
public class EvaluatorFactory {

    /**
     * 静态的求值器工厂
     */
    private static EvaluatorFactory INSTANCE = new EvaluatorFactory();

    /**
     *
     */
    private final Map<String, EvaluatorProvider> providers = new ConcurrentHashMap<String, EvaluatorProvider>();

    /**
     * 私有的构造方法，默认求值器提供者，提供如下的求值器
     */
    private EvaluatorFactory() {
        providers.put(XPathEvaluator.SUPPORTED_DATA_MODEL, new XPathEvaluator.XPathEvaluatorProvider());
        providers.put(JSEvaluator.SUPPORTED_DATA_MODEL, new JSEvaluator.JSEvaluatorProvider());
        providers.put(GroovyEvaluator.SUPPORTED_DATA_MODEL, new GroovyEvaluator.GroovyEvaluatorProvider());
        providers.put(JexlEvaluator.SUPPORTED_DATA_MODEL, new JexlEvaluator.JexlEvaluatorProvider());
        providers.put(MinimalEvaluator.SUPPORTED_DATA_MODEL, new MinimalEvaluator.MinimalEvaluatorProvider());
        providers.put(Evaluator.DEFAULT_DATA_MODEL, providers.get(JexlEvaluator.SUPPORTED_DATA_MODEL));
    }

    /**
     * 设置默认的提供者
     *
     * @param defaultProvider
     */
    public static void setDefaultProvider(EvaluatorProvider defaultProvider) {
        INSTANCE.providers.put(Evaluator.DEFAULT_DATA_MODEL, defaultProvider);
    }

    @SuppressWarnings("unused")
    public static EvaluatorProvider getDefaultProvider() {
        return INSTANCE.providers.get(Evaluator.DEFAULT_DATA_MODEL);
    }

    @SuppressWarnings("unused")
    public static EvaluatorProvider getEvaluatorProvider(String datamodelName) {
        return INSTANCE.providers.get(datamodelName == null ? Evaluator.DEFAULT_DATA_MODEL : datamodelName);
    }

    @SuppressWarnings("unused")
    public static void registerEvaluatorProvider(EvaluatorProvider provider) {
        INSTANCE.providers.put(provider.getSupportedDatamodel(), provider);
    }

    @SuppressWarnings("unused")
    public static void unregisterEvaluatorProvider(String datamodelName) {
        INSTANCE.providers.remove(datamodelName == null ? Evaluator.DEFAULT_DATA_MODEL : datamodelName);
    }

    /**
     * Returns a dedicated Evaluator instance for a specific SCXML document its documentmodel.
     * <p>If no SCXML document is provided a default Evaluator will be returned.</p>
     * <p/>
     * <p/>
     * 静态方法，返回一个专用的求值器实例，对一个指定的SCXML 文档，
     * 如果SCXML文档为空，返回一个默认的求值器
     *
     * @param document The document to return a dedicated Evaluator for. May be null to retrieve the default Evaluator.
     * @return a new and not sharable Evaluator instance for the provided document, or a default Evaluator otherwise
     * @throws ModelException If the SCXML document datamodel is not supported.
     */
    public static Evaluator getEvaluator(SCXML document) throws ModelException {
        String datamodelName = document != null ? document.getDatamodelName() : null;
        EvaluatorProvider provider = INSTANCE.providers.get(datamodelName == null ? Evaluator.DEFAULT_DATA_MODEL : datamodelName);
        if (provider == null) {
            throw new ModelException("Unsupported SCXML document datamodel \"" + (datamodelName) + "\"");
        }
        return document != null ? provider.getEvaluator(document) : provider.getEvaluator();
    }
}
