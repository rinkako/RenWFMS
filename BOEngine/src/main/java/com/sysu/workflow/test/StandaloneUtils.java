//
//package com.sysu.workflow.test;
//
//import com.sysu.workflow.Context;
//import com.sysu.workflow.Evaluator;
//import com.sysu.workflow.SCXMLExecutor;
//import com.sysu.workflow.TriggerEvent;
//import com.sysu.workflow.env.Tracer;
//import com.sysu.workflow.invoke.SimpleSCXMLInvoker;
//import com.sysu.workflow.io.SCXMLReader;
//import com.sysu.workflow.io.SCXMLWriter;
//import com.sysu.workflow.model.ModelException;
//import com.sysu.workflow.model.SCXML;
//
//import javax.xml.stream.XMLStreamException;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.util.StringTokenizer;
//
///**
// * Utility methods used by command line SCXML execution, useful for
// * debugging.
// * <p/>
// * The following expression languages are supported in SCXML documents:
// * <ol>
// * <li>JEXL - Using Commons JEXL</li>
// * </ol>
// *
// * @see com.sysu.workflow.env.jexl
// */
//public final class StandaloneUtils {
//
//    /**
//     * Command line utility method for executing the state machine defined
//     * using the SCXML document described by the specified URI and using
//     * the specified expression evaluator.
//     *
//     * @param uri       The URI or filename of the SCXML document
//     * @param evaluator The expression evaluator for the expression language
//     *                  used in the specified SCXML document
//     *                  <p/>
//     *                  <p>RUNNING:</p>
//     *                  <ul>
//     *                  <li>Enter a space-separated list of "events"</li>
//     *                  <li>To quit, enter "quit"</li>
//     *                  <li>To populate a variable in the current context,
//     *                  type "name=value"</li>
//     *                  <li>To reset state machine, enter "reset"</li>
//     *                  </ul>
//     */
//    public static void execute(final String uri, final Evaluator evaluator) {
//        try {
//            String documentURI = getCanonicalURI(uri);
//            Context rootCtx = evaluator.newContext(null);
//            Tracer trc = new Tracer();
//            SCXML doc = SCXMLReader.read(new URL(documentURI));
//            if (doc == null) {
//                System.err.println("The SCXML document " + uri
//                        + " can not be parsed!");
//                System.exit(-1);
//            }
//            System.out.println(SCXMLWriter.write(doc));
//            SCXMLExecutor exec = new SCXMLExecutor(evaluator, null, trc);
//            exec.setStateMachine(doc);
//            exec.addListener(doc, trc);
//            exec.registerInvokerClass("scxml", SimpleSCXMLInvoker.class);
//            exec.setRootContext(rootCtx);
//            exec.go();
//            BufferedReader br = new BufferedReader(new
//                    InputStreamReader(System.in));
//            String event;
//            while ((event = br.readLine()) != null) {
//                event = event.trim();
//                if (event.equalsIgnoreCase("help") || event.equals("?")) {
//                    System.out.println("Enter a space-separated list of "
//                            + "events");
//                    System.out.println("To populate a variable in the "
//                            + "current context, type \"name=value\"");
//                    System.out.println("To quit, enter \"quit\"");
//                    System.out.println("To reset state machine, enter "
//                            + "\"reset\"");
//                } else if (event.equalsIgnoreCase("quit")) {
//                    break;
//                } else if (event.equalsIgnoreCase("reset")) {
//                    exec.reset();
//                } else if (event.indexOf('=') != -1) {
//                    int marker = event.indexOf('=');
//                    String name = event.substring(0, marker);
//                    String value = event.substring(marker + 1);
//                    rootCtx.setLocal(name, value);
//                    System.out.println("Set variable " + name + " to "
//                            + value);
//                } else if (event.trim().length() == 0
//                        || event.equalsIgnoreCase("null")) {
//                    TriggerEvent[] evts = {new TriggerEvent(null,
//                            TriggerEvent.SIGNAL_EVENT, null)};
//                    exec.triggerEvents(evts);
//                    if (exec.getStatus().isFinal()) {
//                        System.out.println("A final configuration reached.");
//                    }
//                } else {
//                    StringTokenizer st = new StringTokenizer(event);
//                    int tkns = st.countTokens();
//                    TriggerEvent[] evts = new TriggerEvent[tkns];
//                    for (int i = 0; i < tkns; i++) {
//                        evts[i] = new TriggerEvent(st.nextToken(),
//                                TriggerEvent.SIGNAL_EVENT, null);
//                    }
//                    exec.triggerEvents(evts);
//                    if (exec.getStatus().isFinal()) {
//                        System.out.println("A final configuration reached.");
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ModelException e) {
//            e.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param uri an absolute or relative URL
//     * @return java.lang.String canonical URL (absolute)
//     * @throws IOException if a relative URL can not be resolved
//     *                     to a local file
//     */
//    private static String getCanonicalURI(final String uri)
//            throws IOException {
//        if (uri.toLowerCase().startsWith("http://")
//                || uri.toLowerCase().startsWith("file://")) {
//            return uri;
//        }
//        File in = new File(uri);
//        return "file:///" + in.getCanonicalPath();
//    }
//
//    /**
//     * Discourage instantiation since this is a utility class.
//     */
//    private StandaloneUtils() {
//        super();
//    }
//
//}
//
