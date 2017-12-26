
package com.sysu.workflow.model;

import com.sysu.workflow.*;
import com.sysu.workflow.invoke.Invoker;
import com.sysu.workflow.invoke.InvokerException;
import com.sysu.workflow.semantics.ErrorConstants;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * SCXML文档里面的  invoke元素
 * The class in this SCXML object model that corresponds to the
 * &lt;invoke&gt; SCXML element.
 */
public class Invoke extends NamelistHolder implements PathResolverHolder, ContentContainer {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前SCXML执行上下文的key
     * The default context variable key under which the current SCXMLExecutionContext is provided
     */
    private static final String CURRENT_EXECUTION_CONTEXT_KEY = "_CURRENT_EXECUTION_CONTEXT";

    /**
     * 标识符
     * Identifier for this Invoke.
     */
    private String id;

    /**
     * 一个路径表达式，被求值成在先前文档定义中的位置。
     * Path expression evaluating to a location within a previously defined XML data tree.
     */
    private String idlocation;

    /**
     * 被调用目标的类型
     * The type of target to be invoked.
     */
    private String type;

    /**
     * 一个表达式，指出了被调用目标的类型
     * An expression defining the type of the target to be invoked.
     */
    private String typeexpr;

    /**
     * 外部服务的URL
     * The source URL for the external service.
     */
    private String src;

    /**
     * 表达式，求值成一个外部的URL，
     * The expression that evaluates to the source URL for the
     * external service.
     */
    private String srcexpr;

    /**
     * 是否要由  父状态机转发事件到  被调用的（子状态机）
     * A flag indicating whether to forward events to the invoked process.
     */
    private Boolean autoForward;

    /**
     * finalize  孩子元素
     * The &lt;finalize&gt; child, may be null.
     */
    private Finalize finalize;

    /**
     * 一个路径解析器
     * {@link PathResolver} for resolving the "src" or "srcexpr" result.
     */
    private PathResolver pathResolver;

    /**
     * 被调用的元素的内容（一些参数吧）
     * The &lt;content/&gt; of this invoke
     */
    private Content content;

    /**
     * 当期invoke元素所处的 state
     */
    private EnterableState parent;

    /**
     * 得到invoke的标识符
     * Get the identifier for this invoke (may be null).
     *
     * @return Returns the id.
     */
    public final String getId() {
        return id;
    }

    /**
     * 设置标识符
     * Set the identifier for this invoke.
     *
     * @param id The id to set.
     */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * 返回idlocation
     * @return the idlocation
     */
    public String getIdlocation() {
        return idlocation;
    }

    /**
     * 设置位置表达式
     *
     * @param idlocation The idlocation expression
     */
    public void setIdlocation(final String idlocation) {
        this.idlocation = idlocation;
    }

    /**
     * Get the type for this &lt;invoke&gt; element.
     *
     * @return String Returns the type.
     */
    public final String getType() {
        return type;
    }

    /**
     * Set the type for this &lt;invoke&gt; element.
     *
     * @param type The type to set.
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /**
     * @return The type expression
     */
    public String getTypeexpr() {
        return typeexpr;
    }

    /**
     * Sets the type expression
     *
     * @param typeexpr The type expression to set
     */
    public void setTypeexpr(final String typeexpr) {
        this.typeexpr = typeexpr;
    }

    /**
     * Get the URL for the external service.
     *
     * @return String The URL.
     */
    public final String getSrc() {
        return src;
    }

    /**
     * Set the URL for the external service.
     *
     * @param src The source URL.
     */
    public final void setSrc(final String src) {
        this.src = src;
    }

    /**
     * Get the expression that evaluates to the source URL for the
     * external service.
     *
     * @return String The source expression.
     */
    public final String getSrcexpr() {
        return srcexpr;
    }

    /**
     * Set the expression that evaluates to the source URL for the
     * external service.
     *
     * @param srcexpr The source expression.
     */
    public final void setSrcexpr(final String srcexpr) {
        this.srcexpr = srcexpr;
    }


    /**
     * @return Returns true if all external events should be forwarded to the invoked process.
     */
    public final boolean isAutoForward() {
        return autoForward != null && autoForward;
    }

    /**
     * @return Returns the flag indicating whether to forward events to the invoked process.
     */
    public final Boolean getAutoForward() {
        return autoForward;
    }

    /**
     * Set the flag indicating whether to forward events to the invoked process.
     *
     * @param autoForward the flag
     */
    public final void setAutoForward(final Boolean autoForward) {
        this.autoForward = autoForward;
    }

    /**
     * Get the Finalize for this Invoke.
     *
     * @return Finalize The Finalize for this Invoke.
     */
    public final Finalize getFinalize() {
        return finalize;
    }

    /**
     * Set the Finalize for this Invoke.
     *
     * @param finalize The Finalize for this Invoke.
     */
    public final void setFinalize(final Finalize finalize) {
        this.finalize = finalize;
    }

    /**
     * Get the {@link PathResolver}.
     *
     * @return Returns the pathResolver.
     */
    public PathResolver getPathResolver() {
        return pathResolver;
    }

    /**
     * Set the {@link PathResolver}.
     *
     * @param pathResolver The pathResolver to set.
     */
    public void setPathResolver(final PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    /**
     * Enforce identity equality only
     *
     * @param other other object to compare with
     * @return this == other
     */
    @Override
    public final boolean equals(final Object other) {
        return this == other;
    }

    /**
     * Enforce returning identity based hascode
     *
     * @return {@link System#identityHashCode(Object) System.identityHashCode(this)}
     */
    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }

    /**
     * Returns the content
     *
     * @return the content
     */
    public Content getContent() {
        return content;
    }

    /**
     * 返回local context 变量名字，
     * @return The local context variable name under which the current SCXMLExecutionContext is provided to the Invoke
     */
    public String getCurrentSCXMLExecutionContextKey() {
        return CURRENT_EXECUTION_CONTEXT_KEY;
    }

    /**
     * Sets the content
     *
     * @param content the content to set
     */
    public void setContent(final Content content) {
        this.content = content;
    }

    /**
     * Get the parent EnterableState.
     *
     * @return Returns the parent state
     */
    public EnterableState getParentEnterableState() {
        return parent;
    }

    /**
     * Set the parent EnterableState.
     *
     * @param parent The parent state to set
     */
    public void setParentEnterableState(final EnterableState parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent parameter cannot be null");
        }
        this.parent = parent;
    }

    /**
     * 执行函数
     * @param axctx action执行上下文
     * @throws ModelException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void execute(final ActionExecutionContext axctx) throws ModelException {
        //得到invoke所在的状态
        EnterableState parentState = getParentEnterableState();

        //得到状态所在的上下文
        Context ctx = axctx.getContext(parentState);

        //得到当前状态机的执行上下文
        SCXMLExecutionContext exctx = (SCXMLExecutionContext) ctx.getVars().get(getCurrentSCXMLExecutionContextKey());
        if (exctx == null) {  //状态机的执行上下文为空，抛出异常
            throw new ModelException("Missing current SCXMLExecutionContext instance in context under key: " + getCurrentSCXMLExecutionContextKey());
        }
        try {
            //状态所在的上下文添加命名空间
            ctx.setLocal(getNamespacesKey(), getNamespaces());
            //得到求值器
            Evaluator eval = axctx.getEvaluator();

            //
            String typeValue = type;
            //类型为空，对类型表达式求值
            if (typeValue == null && typeexpr != null) {
                //将表达式的值求出来，
                typeValue = (String) getTextContentIfNodeResult(eval.eval(ctx, typeexpr));
                //球之后的表达式结果是空的，抛出异常
                if (typeValue == null) {
                    throw new SCXMLExpressionException("<invoke> for state " + parentState.getId() +
                            ": type expression \"" + typeexpr + "\" evaluated to null or empty String");
                }
            }

            //如果在文档中type 和typeexpr都没有指定，默认类型就是调用一个scxml会话
            if (typeValue == null) {
                typeValue = SCXMLExecutionContext.SCXML_INVOKER_TYPE;
            }
            //实例化一个调用者
            Invoker invoker = exctx.newInvoker(typeValue);

            //得到当前invoke的id
            String invokeId = getId();
            if (invokeId == null) {

                invokeId = parentState.getId() + "." + ctx.get(SCXMLSystemContext.SESSIONID_KEY);
            }
            //如果idlocation不为空，求IDlocation的值
            if (getId() == null && getIdlocation() != null) {
                eval.evalAssign(ctx, idlocation, invokeId, Evaluator.AssignType.REPLACE_CHILDREN, null);
            }
            //给invoker添加参数
            invoker.setInvokeId(invokeId);

            //得到需要调用的文档的位置
            String src = getSrc();
            if (src == null && getSrcexpr() != null) {
                src = (String) getTextContentIfNodeResult(eval.eval(ctx, getSrcexpr()));
            }
            if (src != null) {
                PathResolver pr = getPathResolver();
                if (pr != null) {
                    src = getPathResolver().resolvePath(src);
                }
            }
            //节点
            Node srcNode = null;
            if (src == null && getContent() != null) {  //如果没有指定src或者src表达式，并且有指定的内容
                Object contentValue;
                if (content.getExpr() != null) {   //content的表达式不为空
                    contentValue = eval.eval(ctx, content.getExpr());
                } else {
                    contentValue = content.getBody();//指定content的内容体
                }
                if (contentValue instanceof Node) {  //如果context 是内容体，
                    srcNode = ((Node) contentValue).cloneNode(true);  //
                } else if (contentValue != null) {
                    src = String.valueOf(contentValue);   //否则的话
                }
            }
            if (src == null && srcNode == null) {
                throw new SCXMLExpressionException("<invoke> for state " + parentState.getId() +
                        ": no src and no content defined");
            }
            Map<String, Object> payloadDataMap = new HashMap<String, Object>();
            addNamelistDataToPayload(axctx, payloadDataMap);
            addParamsToPayload(axctx, payloadDataMap);
            invoker.setParentSCXMLExecutor(exctx.getSCXMLExecutor());
            if (src != null) {
                invoker.invoke(src, payloadDataMap);
            }
            // TODO: } else { invoker.invoke(srcNode, payloadDataMap); }
            //给上下文注册调用者，当前invoke元素，注册 invoker
            exctx.registerInvoker(this, invoker);

        } catch (InvokerException e) {
            axctx.getErrorReporter().onError(ErrorConstants.EXECUTION_ERROR, e.getMessage(), this);
            axctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
        } catch (SCXMLExpressionException e) {
            axctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
            axctx.getErrorReporter().onError(ErrorConstants.EXPRESSION_ERROR, e.getMessage(), this);
        } finally {
            ctx.setLocal(getNamespacesKey(), null);
        }
    }
}