/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.sysu.workflow.env.MultiStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.env.jexl.JexlEvaluator;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.RInstanceTree;
import org.sysu.workflow.io.BOXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.MessageMode;
import org.sysu.workflow.stateless.RuntimeManagementService;
import org.sysu.workflow.stateless.SteadyStepService;
import org.sysu.workflow.utility.SerializationUtil;

import java.net.URL;

/**
 * Author: Rinkako
 * Date  : 2018/3/6
 * Usage :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StatelessTest {

    @Before
    public void Prepare() {
        GlobalContext.IsLocalDebug = true;
    }

    @Test
    public void Phase1() throws Exception {
        URL url = SCXMLTestHelper.getResource("InitBOTestMain.xml");
        SCXML scxml = new BOXMLReader().read(url);
        Evaluator evaluator = new JexlEvaluator();
        EventDispatcher dispatcher = new MultiStateMachineDispatcher();
        BOXMLExecutor executor = new BOXMLExecutor(evaluator, dispatcher, new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.setRtid("testRTID");

        long startTime=System.currentTimeMillis();
        executor.go();
        long endTime=System.currentTimeMillis();
        System.out.println("COST TIME： " + (endTime-startTime) + "ms");

        RInstanceTree tree = InstanceManager.GetInstanceTree("testRTID");
        BOXMLExecutionContext ctx = executor.getExctx();
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_1", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);

    }

    @Test
    public void Phase2() throws Exception {
        SteadyStepService.ResumeSteady("testRTID");
        RInstanceTree tree = InstanceManager.GetInstanceTree("testRTID");
        EventDispatcher dispatcher = tree.Root.getExect().getEventDispatcher();
        BOXMLExecutor executor = tree.Root.getExect().getSCXMLExecutor();
        BOXMLExecutionContext ctx = executor.getExctx();
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_0", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);
        Assert.assertEquals(tree.Root.getExect().getScInstance().getGlobalContext().getVars().get("finishCount"), 2);
        Assert.assertFalse(executor.getStatus().isFinal());
        String st = SerializationUtil.JsonSerialization(RuntimeManagementService.GetSpanTreeDescriptor("testRTID"), "testRTID");
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.TO_NOTIFIABLE_ID, "InitBOTestSub_2", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "stop", null, "", 0);
        Assert.assertEquals(tree.Root.getExect().getScInstance().getGlobalContext().getVars().get("finishCount"), 3);
        Assert.assertTrue(executor.getStatus().isFinal());
    }

}
