/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sysu.workflow.env.MultiStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.env.jexl.JexlEvaluator;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.RInstanceTree;
import org.sysu.workflow.io.BOXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.MessageMode;
import org.sysu.workflow.restful.service.RuntimeManagementService;
import org.sysu.workflow.utility.SerializationUtil;

import java.net.URL;

/**
 * Author: Rinkako
 * Date  : 2018/3/6
 * Usage :
 */
public class StatelessTest {

    @Before
    public void Prepare() {
        GlobalContext.IsLocalDebug = true;
    }

    @Test
    public void TestGoAll() throws Exception {
        URL url = SCXMLTestHelper.getResource("Attaching.xml");
        SCXML scxml = new BOXMLReader().read(url);
        Evaluator evaluator = new JexlEvaluator();
        EventDispatcher dispatcher = new MultiStateMachineDispatcher();
        BOXMLExecutor executor = new BOXMLExecutor(evaluator, dispatcher, new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.setRtid("testRTID");
        executor.go();
        RInstanceTree tree = InstanceManager.GetInstanceTree("testRTID");
        BOXMLExecutionContext ctx = executor.getExctx();
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        Assert.assertFalse(executor.getStatus().isFinal());
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        Assert.assertTrue(executor.getStatus().isFinal());
    }

    @Test
    public void TestAttachDetach() throws Exception {
        URL url = SCXMLTestHelper.getResource("Attaching.xml");
        SCXML scxml = new BOXMLReader().read(url);
        Evaluator evaluator = new JexlEvaluator();
        EventDispatcher dispatcher = new MultiStateMachineDispatcher();
        BOXMLExecutor executor = new BOXMLExecutor(evaluator, dispatcher, new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.setRtid("testRTID");
        executor.go();
        RInstanceTree tree = InstanceManager.GetInstanceTree("testRTID");
        BOXMLExecutionContext ctx = executor.getExctx();
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        Assert.assertFalse(executor.getStatus().isFinal());


        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "Attaching", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "next", null, "", 0);
        Assert.assertTrue(executor.getStatus().isFinal());
    }
}
