package com.sysu.workflow.model;

import com.sysu.workflow.*;
import com.sysu.workflow.env.MulitStateMachineDispatcher;
import com.sysu.workflow.env.SimpleErrorReporter;
import com.sysu.workflow.env.jexl.JexlEvaluator;
import com.sysu.workflow.io.SCXMLReader;
import org.junit.Test;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/2/28
 * Time: 20:16
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://www.thinerzq.me</a>
 * Email: 601097836@qq.com
 */
public class TestSubStateMachine {

    @Test
    public void testSubStateMachine(){
         /*try {
            URL url = SCXMLTestHelper.getResource("subStateMachine.xml");
            SCXML scxml = new SCXMLReader().read(url);
            //实例化数据模型解析器
            Evaluator evaluator = new JexlEvaluator();

            //实例化引擎
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());

            executor.setStateMachine(scxml);



            Context rootConext = evaluator.newContext(null);

            executor.setRootContext(rootConext);
            executor.go();

            executor.triggerEvent(new TriggerEvent("start2", TriggerEvent.SIGNAL_EVENT,null));
            executor.triggerEvent(new TriggerEvent("decomposeVoteComplete", TriggerEvent.SIGNAL_EVENT,null));

        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
