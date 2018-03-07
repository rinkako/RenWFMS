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
import org.sysu.workflow.io.SCXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.MessageMode;

import java.net.URL;

/**
 * Author: Rinkako
 * Date  : 2018/3/6
 * Usage :
 */
public class InitBOParamTest {

    @Before
    public void Prepare() {
        GlobalContext.IsLocalDebug = true;
    }

    @Test
    public void TestInitBOParam() throws Exception {
        URL url = SCXMLTestHelper.getResource("InitBOTestMain.xml");
        SCXML scxml = new SCXMLReader().read(url);
        Evaluator evaluator = new JexlEvaluator();
        EventDispatcher dispatcher = new MultiStateMachineDispatcher();
        SCXMLExecutor executor = new SCXMLExecutor(evaluator, dispatcher, new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.setRtid("testRTID");
        executor.go();
        RInstanceTree tree = InstanceManager.GetInstanceTree("testRTID");

        SCXMLExecutionContext ctx = executor.getExctx();
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_1", "", SCXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_0", "", SCXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_2", "", SCXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);
        Assert.assertEquals(tree.Root.getExect().getScInstance().getGlobalContext().getVars().get("finishCount"), 3);
        Assert.assertTrue(executor.getStatus().isFinal());
    }
}
