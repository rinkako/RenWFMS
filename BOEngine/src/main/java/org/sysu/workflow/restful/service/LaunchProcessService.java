package org.sysu.workflow.restful.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.workflow.Context;
import org.sysu.workflow.Evaluator;
import org.sysu.workflow.EvaluatorFactory;
import org.sysu.workflow.SCXMLExecutor;
import org.sysu.workflow.env.MulitStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.io.SCXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.Task;
import org.sysu.workflow.model.extend.Tasks;
import org.sysu.workflow.restful.entity.RenBoEntity;
import org.sysu.workflow.restful.entity.RenProcessEntity;
import org.sysu.workflow.restful.entity.RenRstaskEntity;
import org.sysu.workflow.restful.entity.RenRuntimerecordEntity;
import org.sysu.workflow.restful.utility.HibernateUtil;
import org.sysu.workflow.restful.utility.LogUtil;
import org.sysu.workflow.restful.utility.SerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.sysu.workflow.restful.utility.SerializationUtil.DeserializationSCXMLByByteArray;

/**
 * Author: Ariana
 * Date  : 2018/1/22
 * Usage : All process launch service will be handled in this service module.
 */
public final class LaunchProcessService {
    /**
     * obtain main bo xml content from database according to the process id, and then read and execute it
     * @param rtid the runtime record of a process
     */
    public static void LaunchProcess(String rtid) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            //根据process id从db中找到该process关联的bo(可能是多个)
//            List pbResult = session.createQuery(String.format("FROM RenProcessboEntity WHERE pid = '%s'", pid)).list();
//            for (Object pb : pbResult) {
//                RenProcessboEntity renProcessboEntity = (RenProcessboEntity) pb;
//                String boid = renProcessboEntity.getBoid();
//                //根据bo id找到root bo的content
//                if(boid.equals(roid)) {
//                    List boResult = session.createQuery(String.format("FROM RenBoEntity WHERE boid = '%s'", boid)).list();
//                    for (Object bo : boResult) {
//                        RenBoEntity boEntity = (RenBoEntity) bo;
//                        String boContent = boEntity.getBoContent();
//                        //read bo content and then go it
//                        LaunchProcessService.ExecuteBO(boContent);
//                        break;
//                    }
//                    break;
//                }
//            }
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            assert rre != null;
            String pid = rre.getProcessId();
            RenProcessEntity rpe = session.get(RenProcessEntity.class, pid);
            assert rpe != null;
            String mainBO = rpe.getMainBo();
            List boList = session.createQuery(String.format("FROM RenBoEntity WHERE pid = '%s'", pid)).list();
            for(Object bo:boList) {
                RenBoEntity boEntity = (RenBoEntity) bo;
                if(boEntity.getBoName().equals(mainBO)) {
//                    String boContent = boEntity.getBoContent();
//                    ExecuteBO(boContent);
                    byte[] serializedBO = boEntity.getSerialized();
                    SCXML DeserializedBO = DeserializationSCXMLByByteArray(serializedBO);
                    LaunchProcessService.ExecuteBO(DeserializedBO, rtid, pid);
                    break;
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.Log("When read bo by rtid, exception occurred, " + e.toString() + ", service rollback",
                    LaunchProcessService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            transaction.rollback();
        }
    }

    /**
     * execute the main bo of the current process
     * @param scxml
     * @param rtid
     * @param pid
     */
    public static void ExecuteBO(SCXML scxml, String rtid, String pid) {
        try {
//          Evaluator evaluator = new JexlEvaluator();
            Evaluator evaluator = EvaluatorFactory.getEvaluator(scxml);
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());
            //初始化执行上下文
            Context rootContext = evaluator.newContext(null);
            executor.setRootContext(rootContext);
            executor.setRtid(rtid);
            executor.setPid(pid);
            executor.setStateMachine(scxml);
            executor.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize a list of BO by their id and return involved business role names.
     * @param boidList BOs to be serialized
     * @return HashSet of Involved business role name
     */
    public static HashSet<String> SerializeBO(String boidList) {
        HashSet<String> retSet = new HashSet<String>();
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            String[] boidItems = boidList.split(",");
            for (String boid : boidItems) {
                RenBoEntity rbe = session.get(RenBoEntity.class, boid);
                assert rbe != null;
                SCXML scxml = LaunchProcessService.ParseStringToSCXML(rbe.getBoContent());
                if (scxml == null) {
                    continue;
                }
                HashSet<String> oneInvolves = LaunchProcessService.GetInvolvedBusinessRole(scxml);
                retSet.addAll(oneInvolves);
                rbe.setBroles(SerializationUtil.JsonSerialization(oneInvolves, ""));
                rbe.setSerialized(SerializationUtil.SerializationSCXMLToByteArray(scxml));
                Tasks tasks = scxml.getTasks();
                for (Task t : tasks.getTaskList()) {
                    RenRstaskEntity rrte = new RenRstaskEntity();
                    rrte.setBoid(boid);
                    rrte.setTaskid(String.format("TSK_%s", UUID.randomUUID().toString()));
                    rrte.setHookdescriptor("");  // todo
                    rrte.setEventdescriptor(String.format("{\"OnComplete\":\"%s\"}", t.getEvent()));  // todo other event
                    rrte.setDocumentation("");  // todo
                    rrte.setPrinciple(t.getPrinciple());
                    rrte.setPolymorphismId(t.getId());
                    rrte.setPolymorphismName(t.getName());
                    rrte.setBrole(t.getBrole());
                    rrte.setParameters("");  // todo
                    session.save(rrte);
                }
            }
            transaction.commit();
            return retSet;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("When serialize BOList(%s), exception occurred, %s, service rollback", boidList, ex),
                    LaunchProcessService.class.getName(), LogUtil.LogLevelType.ERROR, boidList);
            transaction.rollback();
        }
        return retSet;
    }

    /**
     * Interpret XML string to SCXML instance.
     * @param boXMLContent BO XML string
     * @return {@code SCXML} instance
     */
    private static SCXML ParseStringToSCXML(String boXMLContent) {
        try {
            InputStream inputStream = new ByteArrayInputStream(boXMLContent.getBytes());
            return SCXMLReader.read(inputStream);
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("When read BO XML data, exception occurred, %s", ex),
                    LaunchProcessService.class.getName(), LogUtil.LogLevelType.ERROR, boXMLContent);
        }
        return null;
    }

    /**
     * Get involved business role name of one BO.
     * @param scxml BO {@code SCXML} instance.
     * @return HashSet of involved business role name
     */
    private static HashSet<String> GetInvolvedBusinessRole(SCXML scxml) {
        HashSet<String> retSet = new HashSet<String>();
        ArrayList<Task> taskList = scxml.getTasks().getTaskList();
        for (Task task : taskList) {
            retSet.add(task.getBrole());
        }
        return retSet;
    }
}