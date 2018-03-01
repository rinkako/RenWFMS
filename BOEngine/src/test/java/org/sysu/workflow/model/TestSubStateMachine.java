package org.sysu.workflow.model;

import org.junit.Test;

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
            //ʵ��������ģ�ͽ�����
            Evaluator evaluator = new JexlEvaluator();

            //ʵ��������
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MultiStateMachineDispatcher(), new SimpleErrorReporter());

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
