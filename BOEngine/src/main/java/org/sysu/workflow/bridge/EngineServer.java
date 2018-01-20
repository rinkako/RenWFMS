package org.sysu.workflow.bridge;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Rinkako on 2017/6/10.
 * 服务器主线程
 */
public class EngineServer {
    public static void AsyncBeginAccept() {
        while(true){
            Socket socket = null;
            try{
                //向操作系统注册服务   port:服务端要监听的端口
                ServerSocket ss = new ServerSocket(8828);
                /*主线程循环执行ServerSocket.accept()
                当拿到客户端连接请求的时候，就会将Socket对象传递给多线程，让多线程去执行具体的操作
                 */
                socket = ss.accept();
                Runnable esh = new EngineServerHandler(socket);
                Thread t = new Thread(esh);//创建线程
                t.start();//启动线程
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
