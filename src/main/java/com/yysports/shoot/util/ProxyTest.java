package com.yysports.shoot.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * 注意：下面代码仅仅实现HTTP请求链接，每一次请求都是无状态保留的，仅仅是这次请求是更换IP的，如果下次请求的IP地址会改变
 * 如果是多线程访问的话，只要将下面的代码嵌入到你自己的业务逻辑里面，那么每次都会用新的IP进行访问，如果担心IP有重复，
 * 自己可以维护IP的使用情况，并做校验。
 *
 * JDK 8u111版本后环境下：要访问的目标页面为HTTPS协议时，需修改“jdk.http.auth.tunneling.disabledSchemes”值
 */
public class ProxyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyTest.class);
    // 代理隧道验证信息 H610Q47U70GK14TD 16FB3537A0A2138F
    final static String proxyUser = "H75W5H98L776649D";
    final static String proxyPass = "9C1AE8A94535B4DD";
    //final static String proxyUser = "HE42YFT7Y18603HD";
    //final static String proxyPass = "201FDAC11FE68417";
    final static String proxyServer = "http-dyn.abuyun.com";
    final static int proxyPort      = 9020;

    public static void main(String args[]) throws Exception {

        // 要访问的目标页面
        //String targetUrl = "http://wx.yysports.com/limitelottery/activity";
        String targetUrl = "http://wx.yysports.com/limitelotterybeijing/activity";

        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        try {
            URL url = new URL(targetUrl);

            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));

            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

            // 设置通过代理访问目标页面
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

            // 解析返回数据
            byte[] response = readStream(connection.getInputStream());

            System.out.println(new String(response));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    public static String doGet(String targetUrl,String jwt) {
        //String targetUrl = "http://wx.yysports.com/limitelottery/activity";
        //String targetUrl = "http://wx.yysports.com/limitelottery/activity/registitems";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1OTA1NDYxMiwianRpIjoiMjI4OTI2NjEiLCJzdWIiOiIxMzI2MTg2NTQwNiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.bcAN1LrGQ7m8tXTfox3nkOeuLptU1gpF5csU34O8BnA";
        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = "";
        try {
            URL url = new URL(targetUrl);
            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));
            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            // 设置通过代理访问目标页面
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + jwt);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            /*JSONArray jsonArray = JSONObject.parseArray(sb.toString());
            JSONObject jsStr = JSONObject.parseObject(sb.toString());*/
            result = sb.toString();
            //LOGGER.info("请求返回的信息={}",result);
            //JSONObject jsStr =JSONObject.fromObject(sb.toString());
            // 解析返回数据
           /* byte[] response = readStream(connection.getInputStream());

            System.out.println(new String(response));*/
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(connection != null) {
                    // 断开连接
                    connection.disconnect();
                }
            } catch (Exception e) {
                LOGGER.error("关闭流异常!");
            }
        }
        return result;
    }

    public static String doPost(String targetUrl,String jwt,String json) {
        //String targetUrl = "http://wx.yysports.com/limitelottery/activity";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1OTA1NDYxMiwianRpIjoiMjI4OTI2NjEiLCJzdWIiOiIxMzI2MTg2NTQwNiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.bcAN1LrGQ7m8tXTfox3nkOeuLptU1gpF5csU34O8BnA";
        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        DataOutputStream dataOutputStream = null;
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            URL url = new URL(targetUrl);
            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));
            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            // 设置通过代理访问目标页面
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + jwt);
            connection.connect();
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeChars(json);
            dataOutputStream.flush();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            result = sb.toString();
            LOGGER.info(result);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        } finally {
            try {
                if (null != dataOutputStream) {
                    dataOutputStream.close();
                }
                if(null != reader) {
                    reader.close();
                }
                if (connection != null) {
                    // 断开连接
                    connection.disconnect();
                }
            } catch (Exception e) {
                LOGGER.error("关闭流异常!");
            }
        }
        return result;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();

        return outSteam.toByteArray();
    }
}
