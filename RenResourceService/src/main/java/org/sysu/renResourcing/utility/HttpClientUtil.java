/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage : Static methods for HTTP request sending.
 */
public class HttpClientUtil {
    /**
     * Send HTTP request by GET method.
     * @param sendUrl url to send
     * @param rtid process rtid
     * @return response string
     */
    public static String SendGet(String sendUrl, String rtid) throws Exception {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(sendUrl);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception ex) {
            LogUtil.Log("Exception occur when send http get request, " + ex,
                    HttpClientUtil.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex2) {
                LogUtil.Log("Exception occur when close http get request sending connection, " + ex2,
                        HttpClientUtil.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            }
        }
    }

    /**
     * Send HTTP request by POST method.
     * @param sendUrl url to send
     * @param args arguments to be encoded
     * @param rtid process rtid
     * @return response string
     */
    public static String SendPost(String sendUrl, Map<String, String> args, String rtid) throws Exception {
        try {
            URL url = new URL(sendUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            StringBuilder sb = new StringBuilder();
            for (Map.Entry entry : args.entrySet()) {
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                sb.append(key).append("=").append(val).append("&");
            }
            String paraStr = sb.toString().substring(0, sb.length() - 1);
            out.append(paraStr);
            out.flush();
            out.close();
            int code = connection.getResponseCode();
            InputStream is = code == 200 ? connection.getInputStream() : connection.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf8"));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            out.close();
            in.close();
            return result.toString();
        } catch (Exception ex) {
            LogUtil.Log("Exception occur when send http post request, " + ex,
                    HttpClientUtil.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;
        }
    }
}
