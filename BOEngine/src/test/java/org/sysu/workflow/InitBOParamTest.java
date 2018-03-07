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
        Assert.assertTrue(executor.getStatus().isFinal());
    }
}
