
package com.sysu.workflow.model;

import com.sysu.workflow.NotificationRegistry;

/**
 * SCXML对象模型中元素的接口，
 * 过程能够使用NotificationRegistry观测到，
 * 包括
 * {@link TransitionTarget}
 * {@link Transition}
 * {@link EnterableState}
 * {@link Final}
 * {@link History}
 * {@link Parallel}
 * {@link SCXML}
 * {@link State}
 * {@link TransitionalState}
 * <p/>
 * <p>Note:这里假设没有多余Integer.MAXINTEGER 个元素在一个SCXML文档里面</p>
 */
public interface Observable {

    /**
     * @return 返回被观察对象的ID，id值是唯一的。
     */
    Integer getObservableId();
}

