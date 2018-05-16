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
import org.sysu.workflow.io.BOXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.MessageMode;

import java.net.URL;
import java.util.HashMap;

/**
 * Author: Rinkako
 * Date  : 2018/3/6
 * Usage :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ForeachTest {

    @Before
    public void Prepare() {
        GlobalContext.IsLocalDebug = true;
    }

    @Test
    public void TestForeach() throws Exception {
        URL url = SCXMLTestHelper.getResource("foreach.xml");
        SCXML scxml = new BOXMLReader().read(url);
        Evaluator evaluator = new JexlEvaluator();
        EventDispatcher dispatcher = new MultiStateMachineDispatcher();
        BOXMLExecutor executor = new BOXMLExecutor(evaluator, dispatcher, new SimpleErrorReporter());
        executor.setStateMachine(scxml);
        executor.setRtid("testRTID");
        executor.go();

        BOXMLExecutionContext ctx = executor.getExctx();
        HashMap<String, String> argDict = new HashMap<>();
        argDict.put("1", "Step1");
        argDict.put("2", "Step2");
        argDict.put("3", "Step3");
        dispatcher.send("testRTID", ctx.NodeId, "", MessageMode.MULTICAST, "foreach", "", BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR,
                "test", argDict, "", 0);
        Assert.assertTrue(executor.getStatus().isFinal());
    }
}
