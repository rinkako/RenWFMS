package com.sysu.workflow.model.extend;

import com.sysu.workflow.*;
import com.sysu.workflow.engine.InstanceManager;
import com.sysu.workflow.engine.TimeInstanceTree;
import com.sysu.workflow.engine.TimeTreeNode;
import com.sysu.workflow.env.MulitStateMachineDispatcher;
import com.sysu.workflow.env.SimpleErrorReporter;
import com.sysu.workflow.io.SCXMLReader;
import com.sysu.workflow.model.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhengshouzi on 2016/1/2.
 * Modified by Rinkako on 2017/3/7.
 */
public class SubStateMachine extends NamelistHolder implements PathResolverHolder {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The file source of this state machine
     */
    private String src;

    /**
     * How many sub state machine instance ought to be create
     */
    private int instances = 1;

    /**
     * Path Resolver for the file src
     * {@link PathResolver} for resolving the "src" or "srcexpr" result.
     */
    private PathResolver pathResolver;

    /**
     * Get the value of src
     * @return value of src property
     */
    public String getSrc() {
        return src;
    }

    /**
     * Set the value of src
     * @param src the src value to set
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Get the value of instance
     * @return value of instance property
     */
    public int getInstances() {
        return instances;
    }

    /**
     * Set the value of instance
     * @param instances the instance value to set, represent how many sub instance ought to be created
     */
    public void setInstances(int instances) {
        this.instances = instances;
    }

    /**
     * Get the value of pathResolver
     * @return value of pathResolver property
     */
    public PathResolver getPathResolver() {
        return pathResolver;
    }

    /**
     * Set the value of pathResolver
     * @param pathResolver The path resolver to use.
     */
    public void setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    /**
     * Execution of encountering this label
     * @param exctx The ActionExecutionContext for this execution instance
     * @throws ModelException
     * @throws SCXMLExpressionException
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        try {
            EnterableState parentState = getParentEnterableState();
            Context ctx = exctx.getContext(parentState);
            ctx.setLocal(getNamespacesKey(), getNamespaces());
            Map<String, Object> payloadDataMap = new LinkedHashMap<String, Object>();
            addParamsToPayload(exctx, payloadDataMap);
            // get resource file url
            //final URL url = this.getClass().getClassLoader().getResource(getSrc());

            // RINKAKO: get file by passing URL
            URL url = new URL("file", "", getSrc());
            try {
                InputStream in = url.openStream();
            } catch (Exception e1) {
                System.out.println("load file directly failed, try get resource");
                url = this.getClass().getClassLoader().getResource(getSrc());
            }

            SCXML scxml = null;
            // init sub state machine SCXML object
            try {
                scxml = SCXMLReader.read(url);
            } catch (Exception e) {
                System.out.println("couldn't find :" + getSrc());
                e.printStackTrace();
            }
            // launch sub state machine of the number of instances
            SCXMLExecutionContext currentExecutionContext = (SCXMLExecutionContext) exctx.getInternalIOProcessor();
            TimeInstanceTree iTree = InstanceManager.GetInstanceTree(currentExecutionContext.RootTid);
            TimeTreeNode curNode = iTree.GetNodeById(currentExecutionContext.Tid);
            for (int i = 0; i < getInstances(); i++) {
                Evaluator evaluator = EvaluatorFactory.getEvaluator(scxml);
                SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter(), null, currentExecutionContext.RootTid);
                executor.setStateMachine(scxml);
                System.out.println("Create sub state machine from: " + url.getFile());
                // init execution context
                Context rootContext = evaluator.newContext(null);
                for (Map.Entry<String,Object> entry : payloadDataMap.entrySet()){
                    rootContext.set(entry.getKey(), entry.getValue());
                }
                executor.setRootContext(rootContext);
                executor.setExecutorIndex(iTree.Root.getExect().getSCXMLExecutor().getExecutorIndex());
                // start dash sub state machine
                executor.go();
                // maintain the relation of this sub state machine on the instance tree

                TimeTreeNode subNode = new TimeTreeNode(executor.getExctx().getStateMachine().getName(), executor.Tid, executor.getExctx(), curNode);
                curNode.AddChild(subNode);

                //String currentSessionId = (String) currentExecutionContext.getScInstance().getSystemContext().get(SCXMLSystemContext.SESSIONID_KEY);
                //String subStateMachineSessionId = (String) executor.getGlobalContext().getSystemContext().get(SCXMLSystemContext.SESSIONID_KEY);
                //instanceTree.insert(currentSessionId, subStateMachineSessionId, executor.getStateMachine().getName());
                // add this new executor to the instance manager
                //SCXMLInstanceManager.setSCXMLInstance(executor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
