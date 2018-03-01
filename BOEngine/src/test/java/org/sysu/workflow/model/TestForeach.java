package org.sysu.workflow.model;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/2/21
 * Time: 21:45
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://www.thinerzq.me</a>
 * Email: 601097836@qq.com
 */
public class TestForeach {
    @Test
    public void testForeach(){
        /*try {
            URL url = SCXMLTestHelper.getResource("foreach.xml");
            SCXML scxml = new SCXMLReader().read(url);
            //ʵ��������ģ�ͽ�����
            Evaluator evaluator = new JexlEvaluator();

            //ʵ��������
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MultiStateMachineDispatcher(), new SimpleErrorReporter());

            executor.setStateMachine(scxml);

            Set<CrowdSourcingTaskEntity> strings = new HashSet<CrowdSourcingTaskEntity>();
            CrowdSourcingTaskEntity cst = new CrowdSourcingTaskEntity();
            cst.setTaskName("crowdsourcing taskName");
            cst.setTaskDescription("crowdsourcing taskDescription");
            strings.add(cst);


            Context rootConext = evaluator.newContext(null);

            executor.setRootContext(rootConext);
            rootConext.set("crowdSourcingTaskEntity", cst);
            executor.go();
            rootConext.set("stringSet", strings);
            rootConext.set("CrowdSourcingTaskEntity",CrowdSourcingTaskEntity.class);

            executor.triggerEvent(new TriggerEvent("test",TriggerEvent.SIGNAL_EVENT,null));

        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
