/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception ex) {
                // nothing
            }
        }
    }

    /**
     * Send HTTP request by POST method.
     * NOTICE DO NOT use this method directly, instead use {@code InteractionRouter}
     * @param sendUrl url to send
     * @param args arguments to be encoded
     * @param rtid process rtid
     * @return response string
     */
    public static String SendPost(String sendUrl, Map<String, String> args, String rtid) throws Exception {
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
    }
}
