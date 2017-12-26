package org.sysu.workflow.bridge;

/**
 * Created by Rinkako on 2017/6/10.
 */
public class EngineServer {
    public static void AsyncBeginAccept() {
        Runnable esh = new EngineServerHandler();
        Thread t = new Thread(esh);
        t.start();
    }
}
