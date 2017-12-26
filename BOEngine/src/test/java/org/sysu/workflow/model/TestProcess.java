package org.sysu.workflow.model;

import org.sysu.workflow.*;
import org.sysu.workflow.env.MulitStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.env.jexl.JexlEvaluator;
import org.sysu.workflow.io.SCXMLReader;
import org.junit.Test;

import java.net.URL;

/**
 * Created by LittleHuiHui on 2017/4/15.
 */
public class TestProcess {
    @Test
    public void testProcess(){
        try{
            URL url = SCXMLTestHelper.getResource("subprocess.xml");
            SCXML scxml = new SCXMLReader().read(url);
            //实例化数据模型解析器
            Evaluator evaluator = new JexlEvaluator();
            //实例化引擎
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());

            executor.setStateMachine(scxml);
            Context rootConext = evaluator.newContext(null);

            executor.setRootContext(rootConext);
            executor.go();

            executor.triggerEvent(new TriggerEvent("pick", TriggerEvent.SIGNAL_EVENT,null));
            executor.triggerEvent(new TriggerEvent("paid", TriggerEvent.SIGNAL_EVENT,null));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
