//
//package com.sysu.workflow.test;
//
//import com.sysu.workflow.Evaluator;
//import com.sysu.workflow.env.jexl.JexlEvaluator;
//
///**
// * Standalone SCXML interpreter, useful for command-line testing and
// * debugging, where expressions are JEXL expressions.
// * <p/>
// * <p>USAGE:</p>
// * <p><code>java StandaloneJexlExpressions
// * url</code></p>
// * <p>or</p>
// * <p><code>java StandaloneJexlExpressions
// * filename</code>
// * </p>
// * <p/>
// * <p>RUNNING:</p>
// * <ul>
// * <li>Enter a space-separated list of "events"</li>
// * <li>To quit, enter "quit"</li>
// * <li>To populate a variable in the current context,
// * type "name=value"</li>
// * <li>To reset state machine, enter "reset"</li>
// * </ul>
// */
//public final class StandaloneJexlExpressions {
//
//    /**
//     * Launcher.
//     *
//     * @param args The arguments, one expected, the URI or filename of the
//     *             SCXML document
//     */
//    public static void main(final String[] args) {
//        if (args.length < 1) {
//            System.out.println("USAGE: java "
//                    + StandaloneJexlExpressions.class.getName()
//                    + "<url|filename>");
//            System.exit(-1);
//        }
//        Evaluator evaluator = new JexlEvaluator();
//        StandaloneUtils.execute(args[0], evaluator);
//    }
//
//    /**
//     * Discourage instantiation since this is a utility class.
//     */
//    private StandaloneJexlExpressions() {
//        super();
//    }
//
//}
//
