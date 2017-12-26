package com.sysu.workflow.bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
