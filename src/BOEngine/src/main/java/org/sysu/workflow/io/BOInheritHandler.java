package org.sysu.workflow.io;
import org.sysu.workflow.model.extend.InheritableContext;
import org.sysu.workflow.model.SCXML;

import java.net.URL;
import java.util.Stack;

/**
 * Business Object Inherit Handler Class
 * This class is used to solve inherit relationship of a SCXML instance
 * object, and generate its InheritableContext for the runtime.
 */
public class BOInheritHandler {
    /**
     * Handle inherit relationship for a scxml model object.
     * @param deliver scxml instance
     * @param baseName base scxml of this scxml
     * @return inheritable context for this scxml instance
     */
    public static InheritableContext InheritConnect(SCXML deliver, String baseName) throws Exception {
        if (deliver == null || baseName == null || baseName.length() <= 0) {
            return null;
        }
        Stack<SCXML> inheritObjectStack = new Stack<SCXML>();
        BOInheritHandler.RecursiveInheritHandler(baseName, inheritObjectStack);
        InheritableContext inheritor = new InheritableContext();
        BOInheritHandler.GenerateInheritableContext(inheritor, inheritObjectStack);
        return inheritor;
    }

    /**
     * Generate inherit stack recursively.
     * @param baseName scxml model name
     * @param inheritStack inherit stack
     */
    private static void RecursiveInheritHandler(String baseName, Stack<SCXML> inheritStack) throws Exception {
        URL url = BOInheritHandler.class.getClassLoader().getResource(baseName+".xml");
        System.out.println("url:"+url);
        String nextBase = BOInheritHandler.PushSCXML(url, inheritStack);
        if (nextBase == null || nextBase.length() <= 0) {
            return;
        }
        BOInheritHandler.RecursiveInheritHandler(nextBase, inheritStack);
    }

    /**
     * Activate a scxml instance and push it to inherit stack
     * @param baseUrl URL object for scxml model file
     * @param inheritStack inherit stack
     * @return the base object id for this scxml
     */
    private static String PushSCXML(URL baseUrl, Stack<SCXML> inheritStack) throws Exception {
        SCXML scxml = BOXMLReader.read(baseUrl);
        inheritStack.push(scxml);
        return scxml.getBaseBusinessObjectName();
    }

    /**
     * Generate Inheritable Context from a prepared inherit link stack.
     * @param ctx a empty inheritable context object
     * @param iStack inherit stack
     */
    private static void GenerateInheritableContext(InheritableContext ctx, Stack<SCXML> iStack) {
        while (!iStack.empty()) {
            SCXML baseObj = iStack.pop();
            ctx.UpdateDataModel(baseObj.getDatamodel());
            ctx.UpdateTasks(baseObj.getTasks());
        }
    }
}
