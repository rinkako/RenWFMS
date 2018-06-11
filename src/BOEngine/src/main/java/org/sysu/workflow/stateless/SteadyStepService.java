package org.sysu.workflow.stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.CommonUtil;
import org.sysu.workflow.*;
import org.sysu.workflow.entity.RenArchivedTreeEntity;
import org.sysu.workflow.entity.RenBinstepEntity;
import org.sysu.workflow.env.MultiStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.entity.RenRuntimerecordEntity;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.RInstanceTree;
import org.sysu.workflow.instanceTree.RTreeNode;
import org.sysu.workflow.utility.HibernateUtil;
import org.sysu.workflow.utility.LogUtil;
import org.sysu.workflow.utility.SerializationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Author: Rinkako
 * Date  : 2018/5/15
 * Usage : Methods for making business object stateless.
 */
public class SteadyStepService {

    public static boolean EnableSteadyStep = true;

    /**
     * Write a steady step to steady memory.
     *
     * @param exctx BOXML execution context
     */
    public static void WriteSteady(BOXMLExecutionContext exctx) {
        if (!SteadyStepService.EnableSteadyStep) {
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenBinstepEntity binStep = session.get(RenBinstepEntity.class, exctx.NodeId);
            if (binStep == null) {
                binStep = new RenBinstepEntity();
                binStep.setRtid(exctx.Rtid);
                binStep.setNodeId(exctx.NodeId);
                binStep.setNotifiableId(exctx.NotifiableId);
                RInstanceTree tree = InstanceManager.GetInstanceTree(exctx.Rtid);
                RTreeNode parentNode = tree.GetNodeById(exctx.NodeId).Parent;
                binStep.setSupervisorId(parentNode != null ? parentNode.getExect().NodeId : "");
            }
            BOInstance boInstance = exctx.getSCXMLExecutor().detachInstance();
            binStep.setBinlog(SerializationUtil.SerializationBOInstanceToByteArray(boInstance));
            exctx.getSCXMLExecutor().attachInstance(boInstance);
            session.saveOrUpdate(binStep);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Write stateless steady step to DB failed, save action rollback.",
                    SteadyStepService.class.getName(), LogLevelType.ERROR, exctx.Rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Clear steady step snapshot after final state, and write a span tree descriptor to archived tree table.
     *
     * @param rtid process runtime record id
     */
    public static void ClearSteadyWriteArchivedTree(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createQuery(String.format("DELETE RenBinstepEntity AS p WHERE p.rtid = '%s'", rtid)).executeUpdate();
            RenArchivedTreeEntity archivedTree = new RenArchivedTreeEntity();
            archivedTree.setRtid(rtid);
            archivedTree.setTree(SerializationUtil.JsonSerialization(RuntimeManagementService.GetSpanTreeDescriptor(rtid), rtid));
            session.saveOrUpdate(archivedTree);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Clear stateless steady step failed, action rollback.",
                    SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Resume instances from steady memory, and register it to instance manager.
     *
     * @param rtidList rtid in JSON list
     */
    @SuppressWarnings("unchecked")
    public static List<String> ResumeSteadyMany(String rtidList) {
        List<String> rtidItems = SerializationUtil.JsonDeserialization(rtidList, List.class);
        List<String> failedList = new ArrayList<>();
        for (String rtid : rtidItems) {
            if (!SteadyStepService.ResumeSteady(rtid)) {
                failedList.add(rtid);
            }
        }
        return failedList;
    }

    /**
     * Resume a instance from steady memory, and register it to instance manager.
     *
     * @param rtid process runtime record id
     */
    public static boolean ResumeSteady(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            // update runtime record
            List<RenBinstepEntity> stepItems = session.createQuery(String.format("FROM RenBinstepEntity AS p WHERE p.rtid = '%s'", rtid)).list();
            RenRuntimerecordEntity record = session.get(RenRuntimerecordEntity.class, rtid);
            if (record != null) {
                record.setInterpreterId(GlobalContext.ENGINE_GLOBAL_ID);
                session.saveOrUpdate(record);
            }
            transaction.commit();
            cmtFlag = true;
            // find root node
            RenBinstepEntity rootStep = stepItems.stream().filter(t -> CommonUtil.IsNullOrEmpty(t.getSupervisorId())).findFirst().get();
            String rootNodeId = rootStep.getNodeId();
            // recovery other node
            Stack<RenBinstepEntity> workStack = new Stack<>();
            workStack.push(rootStep);
            while (!workStack.isEmpty()) {
                RenBinstepEntity currentStep = workStack.pop();
                String currentNodeId = currentStep.getNodeId();
                BOInstance curBin = SerializationUtil.DeserializationBOInstanceByByteArray(currentStep.getBinlog());
                Evaluator curEvaluator = EvaluatorFactory.getEvaluator(curBin.getStateMachine());
                BOXMLExecutor curExecutor = new BOXMLExecutor(curEvaluator, new MultiStateMachineDispatcher(), new SimpleErrorReporter());
                curExecutor.NodeId = curExecutor.getExctx().NodeId = currentNodeId;
                curExecutor.RootNodeId = rootNodeId;
                curExecutor.setRootContext(curEvaluator.newContext(null));
                curExecutor.setRtid(rtid);
                if (record != null) {
                    curExecutor.setPid(record.getProcessId());
                }
                curExecutor.attachInstance(curBin);
                curExecutor.setNotifiableId(currentStep.getNotifiableId());
                curExecutor.resume(currentStep.getSupervisorId());
                List<RenBinstepEntity> currentChildren = stepItems.stream().filter(t -> t.getSupervisorId().equals(currentNodeId)).collect(Collectors.toList());
                for (RenBinstepEntity cc : currentChildren) {
                    workStack.push(cc);
                }
            }
            return true;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Resume stateless steady step from DB failed, action rollback.",
                    SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
            return false;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }
}
