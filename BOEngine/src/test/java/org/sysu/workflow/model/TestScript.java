package org.sysu.workflow.model;

import org.junit.Test;

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
            //ʵ��������ģ�ͽ�����
            Evaluator evaluator = new JexlEvaluator();

            //ʵ��������
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
            //ʵ��������ģ�ͽ�����
            Evaluator evaluator = new JexlEvaluator();

            //ʵ��������
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
