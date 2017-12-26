package com.sysu.workflow.model;

import com.sysu.workflow.*;
import com.sysu.workflow.env.MulitStateMachineDispatcher;
import com.sysu.workflow.env.SimpleErrorReporter;
import com.sysu.workflow.env.jexl.JexlEvaluator;
import com.sysu.workflow.io.SCXMLReader;
import org.junit.Test;

import java.net.URL;
import com.sysu.workflow.io.SCXMLReader;
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
