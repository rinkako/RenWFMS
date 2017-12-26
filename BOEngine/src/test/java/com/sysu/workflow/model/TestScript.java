package com.sysu.workflow.model;

import com.sysu.workflow.*;
import com.sysu.workflow.env.MulitStateMachineDispatcher;
import com.sysu.workflow.env.SimpleErrorReporter;
import com.sysu.workflow.env.jexl.JexlEvaluator;
import com.sysu.workflow.io.SCXMLReader;
import com.sysu.workflow.model.SCXML;
import org.junit.Test;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/2/23
 * Time: 14:35
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://www.thinerzq.me</a>
 * Email: 601097836@qq.com
 */
public class TestScript {
    @Test
    public void testScript(){
        /*try {
            URL url = SCXMLTestHelper.getResource("script.xml");
            SCXML scxml = new SCXMLReader().read(url);
            //实例化数据模型解析器
            Evaluator evaluator = new JexlEvaluator();

            //实例化引擎
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());

            executor.setStateMachine(scxml);


            CrowdSourcingTaskEntity cst = new CrowdSourcingTaskEntity();
            cst.setTaskName("crowdsourcing taskName");
            cst.setTaskDescription("crowdsourcing taskDescription");
            Context rootConext = evaluator.newContext(null);
            executor.setRootContext(rootConext);

            rootConext.set("crowdSourcingTaskEntity", cst);
            rootConext.set("CrowdSourcingTaskEntity", CrowdSourcingTaskEntity.class);
            executor.go();



        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
    @Test
    public void testScriptReturn(){
       /* try {
            URL url = SCXMLTestHelper.getResource("script.xml");
            SCXML scxml = new SCXMLReader().read(url);
            //实例化数据模型解析器
            Evaluator evaluator = new JexlEvaluator();

            //实例化引擎
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());

            executor.setStateMachine(scxml);


            CrowdSourcingTaskEntity cst = new CrowdSourcingTaskEntity();
            cst.setTaskName("crowdsourcing taskName");
            cst.setTaskDescription("crowdsourcing taskDescription");
            Context rootConext = evaluator.newContext(null);
            executor.setRootContext(rootConext);

            rootConext.set("crowdSourcingTaskEntity", cst);
            executor.go();



        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
