package com.sysu.workflow.bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rinkako on 2017/6/10.
 */
public class EngineServerHandler implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket ss=new ServerSocket(8828);

            while(true){
                Socket socket=ss.accept();
                BufferedReader bd=new BufferedReader(new InputStreamReader(socket.getInputStream()));

                /**
                 * 接受HTTP请求
                 */
                String requestHeader;
                int contentLength=0;
                while(true){
                    requestHeader=bd.readLine();
                    if (requestHeader == null) {break;}
                    if (requestHeader.isEmpty()) {break;}

                    System.out.println(requestHeader);

                    if(requestHeader.startsWith("GET")){
                        int begin = 4;
                        int end = requestHeader.indexOf("HTTP/") - 1;
                        String condition=requestHeader.substring(begin, end);
                        System.out.println("GET参数是："+condition);
                    }
                    else if(requestHeader.startsWith("POST")){
                        int begin = 5;
                        int end = requestHeader.indexOf("HTTP/") - 1;
                        String condition=requestHeader.substring(begin, end);
                        System.out.println("POST参数是："+condition);
                    }
                    // 获得POST参数
                    if(requestHeader.startsWith("Content-Length")){
                        int begin=requestHeader.indexOf("Content-Lengh:")+"Content-Length:".length();
                        String postParamterLength=requestHeader.substring(begin).trim();
                        contentLength=Integer.parseInt(postParamterLength);
                        System.out.println("POST参数长度是："+Integer.parseInt(postParamterLength));
                    }
                }
                StringBuffer sb=new StringBuffer();
                if(contentLength>0){
                    for (int i = 0; i < contentLength; i++) {
                        sb.append((char)bd.read());
                    }
                    System.out.println("POST参数是："+sb.toString());
                }
                //发送回执
                PrintWriter pw=new PrintWriter(socket.getOutputStream());

                pw.println("HTTP/1.1 200 OK");
                pw.println("Content-type:text/html");
                pw.println();


                Date date=new Date();
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=format.format(date);

                pw.println("<h1>welcome to scxml gateway</h1><br/>Timestamp:" + time);

                pw.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
