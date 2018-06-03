/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.workflow.*;
import org.sysu.workflow.entity.*;
import org.sysu.workflow.env.MultiStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.RInstanceTree;
import org.sysu.workflow.instanceTree.RTreeNode;
import org.sysu.workflow.io.BOXMLReader;
import org.sysu.workflow.model.EnterableState;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.model.extend.Task;
import org.sysu.workflow.model.extend.Tasks;
import org.sysu.workflow.utility.HibernateUtil;
import org.sysu.workflow.utility.LogUtil;
import org.sysu.workflow.utility.SerializationUtil;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


/**
 * Author: Ariana, Rinkako
 * Date  : 2018/1/22
 * Usage : Methods for processes runtime management.
 */
public final class RuntimeManagementService {

    /**
     * obtain main bo xml content from database according to the process id, and then read and execute it
     *
     * @param rtid the runtime record of a process
     */
    public static void LaunchProcess(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            assert rre != null;
            String pid = rre.getProcessId();
            rre.setInterpreterId(GlobalContext.ENGINE_GLOBAL_ID);
            session.save(rre);
            RenProcessEntity rpe = session.get(RenProcessEntity.class, pid);
            assert rpe != null;
            String mainBO = rpe.getMainBo();
            List boList = session.createQuery(String.format("FROM RenBoEntity WHERE pid = '%s'", pid)).list();
            RenBoEntity mainBoEntity = null;
            for (Object bo : boList) {
                RenBoEntity boEntity = (RenBoEntity) bo;
                if (boEntity.getBoName().equals(mainBO)) {
                    mainBoEntity = boEntity;
                    break;
                }
            }
            transaction.commit();
            cmtFlag = true;
            if (mainBoEntity == null) {
                LogUtil.Log("Main BO not exist for launching process: " + rtid,
                        RuntimeManagementService.class.getName(), LogLevelType.ERROR, rtid);
                return;
            }
            byte[] serializedBO = mainBoEntity.getSerialized();
            SCXML DeserializedBO = SerializationUtil.DeserializationSCXMLByByteArray(serializedBO);
            RuntimeManagementService.ExecuteBO(DeserializedBO, rtid, pid);
        } catch (Exception e) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("When read bo by rtid, exception occurred, " + e.toString() + ", service rollback",
                    RuntimeManagementService.class.getName(), LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Serialize a list of BO by their id and return involved business role names.
     *
     * @param boidList BOs to be serialized
     * @return HashSet of Involved business role name
     */
    public static HashSet<String> SerializeBO(String boidList) {
        HashSet<String> retSet = new HashSet<>();
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            String[] boidItems = boidList.split(",");
            for (String boid : boidItems) {
                RenBoEntity rbe = session.get(RenBoEntity.class, boid);
                assert rbe != null;
                SCXML scxml = RuntimeManagementService.ParseStringToSCXML(rbe.getBoContent());
                if (scxml == null) {
                    continue;
                }
                HashSet<String> oneInvolves = RuntimeManagementService.GetInvolvedBusinessRole(scxml);
                retSet.addAll(oneInvolves);
                rbe.setBroles(SerializationUtil.JsonSerialization(oneInvolves, ""));
                rbe.setSerialized(SerializationUtil.SerializationSCXMLToByteArray(scxml));
                Tasks tasks = scxml.getTasks();
                for (Task t : tasks.getTaskList()) {
                    AbstractMap.SimpleEntry<String, String> heDesc = t.GenerateCallbackDescriptor();
                    RenRstaskEntity rrte = new RenRstaskEntity();
                    rrte.setBoid(boid);
                    rrte.setTaskid(String.format("TSK_%s", UUID.randomUUID().toString()));
                    rrte.setHookdescriptor(heDesc.getKey());
                    rrte.setEventdescriptor(heDesc.getValue());
                    rrte.setDocumentation(t.getDocumentation());
                    rrte.setPrinciple(t.getPrinciple().GenerateDescriptor());
                    rrte.setPolymorphismId(t.getId());
                    rrte.setPolymorphismName(t.getName());
                    rrte.setBrole(t.getBrole());
                    rrte.setParameters(t.GenerateParamDescriptor());
                    session.save(rrte);
                }
            }
            transaction.commit();
            return retSet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log(String.format("When serialize BOList(%s), exception occurred, %s, service rollback", boidList, ex),
                    RuntimeManagementService.class.getName(), LogLevelType.ERROR, boidList);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return retSet;
    }

    /**
     * Get a user-friendly descriptor of an instance tree.
     *
     * @param rtid process runtime record id
     * @return a descriptor of span instance tree JSON descriptor
     */
    public static String GetSpanTreeDescriptor(String rtid) {
        RInstanceTree tree = InstanceManager.GetInstanceTree(rtid, false);
        if (tree == null || tree.Root == null) {
            Session session = HibernateUtil.GetLocalSession();
            Transaction transaction = session.beginTransaction();
            RenArchivedTreeEntity rate = null;
            try {
                rate = session.get(RenArchivedTreeEntity.class, rtid);
                transaction.commit();
            }
            catch (Exception ex) {
                transaction.rollback();
            }
            finally {
                HibernateUtil.CloseLocalSession();
            }
            if (rate == null) {
                return null;
            }
            return rate.getTree();
        }
        FriendlyTreeNode rootFNode = new FriendlyTreeNode();
        RuntimeManagementService.Nephren(tree.Root, rootFNode);
        return SerializationUtil.JsonSerialization(rootFNode, rtid);
    }

    /**
     * Recursively handle span the user-friendly package tree of a specific instance tree.
     * This method is to commemorate a girl devoted her love to guard the happiness of who she loved and his lover. -RK
     *
     * @param node current span root node
     * @param fNode user-friendly package node of current span node
     */
    private static void Nephren(@NotNull RTreeNode node, @NotNull FriendlyTreeNode fNode) {
        fNode.BOName = node.getExect().getScInstance().getStateMachine().getName();
        fNode.GlobalId = node.getGlobalId();
        fNode.NotifiableId = node.getExect().NotifiableId;
        Set<EnterableState> status = node.getExect().getScInstance().getCurrentStatus().getActiveStates();
        HashSet<String> stringSet = new HashSet<>();
        for (EnterableState st : status) {
            stringSet.add(st.getId());
        }
        fNode.StatusDescriptor = SerializationUtil.JsonSerialization(stringSet, node.getExect().Rtid);
        for (RTreeNode sub : node.Children) {
            FriendlyTreeNode subFn = new FriendlyTreeNode();
            RuntimeManagementService.Nephren(sub, subFn);
            fNode.Children.add(subFn);
        }
    }

    /**
     * execute the main bo of the current process
     *
     * @param scxml scxml instance
     * @param rtid process rtid
     * @param pid process global id
     */
    private static void ExecuteBO(SCXML scxml, String rtid, String pid) {
        try {
            Evaluator evaluator = EvaluatorFactory.getEvaluator(scxml);
            BOXMLExecutor executor = new BOXMLExecutor(evaluator, new MultiStateMachineDispatcher(), new SimpleErrorReporter());
            Context rootContext = evaluator.newContext(null);
            executor.setRootContext(rootContext);
            executor.setRtid(rtid);
            executor.setPid(pid);
            executor.setStateMachine(scxml);
            executor.go();
        } catch (Exception e) {
            LogUtil.Log("When ExecuteBO, exception occurred, " + e.toString(),
                    RuntimeManagementService.class.getName(), LogLevelType.ERROR, rtid);
        }
    }

    /**
     * Interpret XML string to SCXML instance.
     *
     * @param boXMLContent BO XML string
     * @return {@code SCXML} instance
     */
    private static SCXML ParseStringToSCXML(String boXMLContent) {
        try {
            InputStream inputStream = new ByteArrayInputStream(boXMLContent.getBytes());
            return BOXMLReader.read(inputStream);
        } catch (Exception ex) {
            LogUtil.Log(String.format("When read BO XML data, exception occurred, %s", ex),
                    RuntimeManagementService.class.getName(), LogLevelType.ERROR, boXMLContent);
        }
        return null;
    }

    /**
     * Get involved business role name of one BO.
     *
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

    /**
     * A class for user-friendly tree node data package.
     */
    private static class FriendlyTreeNode {

        public String NotifiableId;

        public String GlobalId;

        public String BOName;

        public String StatusDescriptor;

        public ArrayList<FriendlyTreeNode> Children = new ArrayList<>();
    }
}