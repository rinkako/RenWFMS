package org.sysu.workflow.stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.workflow.*;
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

/**
 * Author: Rinkako
 * Date  : 2018/5/15
 * Usage : Methods for making business object stateless and able to rollback.
 */
public class SteadyStepService {

    /**
     * Write a steady step to steady memory.
     * @param rtid process runtime record id
     * @param exctx BOXML execution context
     */
    public static void WriteSteady(String rtid, BOXMLExecutionContext exctx) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenBinstepEntity binStep = session.get(RenBinstepEntity.class, exctx.NodeId);
            if (binStep == null) {
                binStep = new RenBinstepEntity();
                binStep.setRtid(rtid);
                binStep.setNodeId(exctx.NodeId);
                RInstanceTree tree = InstanceManager.GetInstanceTree(rtid);
                RTreeNode parentNode = tree.GetNodeById(exctx.NodeId).Parent;
                binStep.setSupervisorId(parentNode != null ? parentNode.getExect().NodeId : "");
            }
            BOInstance boInstance = exctx.getSCXMLExecutor().detachInstance();
            binStep.setBinlog(SerializationUtil.SerializationBOInstanceToByteArray(boInstance));
            exctx.getSCXMLExecutor().attachInstance(boInstance);
            session.saveOrUpdate(binStep);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Write stateless steady step to DB failed, save action rollback.",
                    SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Clear steady step snapshot after final state.
     * @param rtid process runtime record id
     */
    public static void ClearSteady(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createQuery(String.format("DELETE RenBinstepEntity AS p WHERE p.rtid = '%s'", rtid)).executeUpdate();
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Clear stateless steady step failed, action rollback.",
                    SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Resume a instance from steady memory, and register it to instance manager.
     * @param rtid process runtime record id
     */
    public static boolean ResumeSteady(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenBinstepEntity binStep = session.get(RenBinstepEntity.class, rtid);
            RenRuntimerecordEntity record = session.get(RenRuntimerecordEntity.class, rtid);
            record.setInterpreterId(GlobalContext.ENGINE_GLOBAL_ID);
            session.saveOrUpdate(record);
            transaction.commit();
            cmtFlag = true;
            if (binStep == null) {
                LogUtil.Log("Resume stateless steady step from DB failed, rtid not exist any rollback snapshot.",
                        SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
                return false;
            }
            try {
                BOInstance bin = SerializationUtil.DeserializationBOInstanceByByteArray(binStep.getBinlog());
                if (bin == null) {
                    LogUtil.Log("Resume stateless steady step from DB failed, deserialized null.",
                            SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
                    return false;
                }
                Evaluator evaluator = EvaluatorFactory.getEvaluator(bin.getStateMachine());
                BOXMLExecutor executor = new BOXMLExecutor(evaluator, new MultiStateMachineDispatcher(), new SimpleErrorReporter());
                executor.setRootContext(evaluator.newContext(null));
                executor.setRtid(rtid);
                executor.setPid(record.getProcessId());
                executor.setStateMachine(bin.getStateMachine());
                executor.go();
                return true;
            } catch (Exception e) {
                LogUtil.Log("When ResumeBO from steady step, exception occurred, " + e.toString(),
                        SteadyStepService.class.getName(), LogLevelType.ERROR, rtid);
                return false;
            }
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
