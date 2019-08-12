package com.yysports.shoot.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
/*import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * HTTP GET请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url,String jwt) {
        String encoding = "utf-8";
        HttpClient httpClient = HttpClients.createDefault();
        String body = null;
        HttpEntity entity = null;
        try {
            URL strUrl = new URL(url);
            URI uri = new URI(strUrl.getProtocol(), strUrl.getHost(), strUrl.getPath(), strUrl.getQuery(), null);
            HttpGet httpget = new HttpGet(uri);
            httpget.setHeader("Content-Type", "application/json;charset=utf-8");
            if(StringUtils.isNoneEmpty(jwt)) {
                httpget.setHeader("Authorization", "Bearer " + jwt);
            }
            httpget.setHeader("Proxy-Authorization", "Basic SDc1VzVIOThMNzc2NjQ5RDo5QzFBRThBOTQ1MzVCNERE");
            /*
             * 设置参数
             */
            httpget.setURI(new URI(httpget.getURI().toString()));
            /*
             * 发送请求
             */
            HttpResponse httpresponse = httpClient.execute(httpget);
            /*
             * 获取返回数据
             */
            entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity, encoding);

        } catch (Exception e) {
            // logger.info("请求异常，异常详情：{}", e);
            e.printStackTrace();
        }
        return body;
    }

    /**
     *
     * @param url
     * @param json
     * @return
     */
    public static String doPostHead(String url, String json) {
        String body = null;
        HttpEntity entity1 = null;
        HttpClient httpClient  = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(json, "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/json;charset=utf-8");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            post.setHeader("x-requested-with", "XMLHttpRequest");
            // logger.info("post:" + post);
            HttpResponse httpresponse = httpClient.execute(post);
            entity1 = httpresponse.getEntity();
            body = EntityUtils.toString(entity1, "UTF-8");
            String resultStr = null;
            //HttpEntity entity2 = httpresponse.getEntity();
            body = EntityUtils.toString(entity, "UTF-8");
            if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject json2 = JSONObject.parseObject(body);
                if (json2.get("errcode") != null) {
                    //resultStr = json.get("errcode").getAsString();
                } else {// 正常情况下
                    resultStr = json2.get("access_token").toString();
                    LOGGER.info(resultStr);
                }
            }
            LOGGER.info(body);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        } finally {
            try {
                EntityUtils.consume(entity1);
                ((CloseableHttpClient) httpClient).close();
            } catch (IOException e) {

            }

        }
        return body;
    }

    /**
     * 发起post请求
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, String json,String jwt) {
        String body = null;
        HttpEntity entity1 = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(json, "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/json");
            if(StringUtils.isNotBlank(jwt)) {
                post.setHeader("Authorization", "Bearer " + jwt);
            }
            HttpResponse httpresponse = httpClient.execute(post);
            entity1 = httpresponse.getEntity();
            body = EntityUtils.toString(entity1, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        } finally {
            try {
                EntityUtils.consume(entity1);
            } catch (IOException e) {
            }
            httpClient.getConnectionManager().shutdown();
        }
        return body;
    }

    @SuppressWarnings("deprecation")
    public static String getToken(String url)
            throws Exception {
        String resultStr = null;
        @SuppressWarnings("resource")
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        //JsonParser jsonparer =JsonParser;// 初始化解析json格式的对象
        // 接收参数json列表
        JSONObject jsonParam = new JSONObject();
        /*jsonParam.put("grant_type", "client_credentials");
        jsonParam.put("client_id", "matrix");
        jsonParam.put("client_secret", "");
        jsonParam.put("username","hemin1997");
        jsonParam.put("password","123456");
        jsonParam.put("response_type","1");
        jsonParam.put("state","2");
        jsonParam.put("redirect_uri","");*/
        jsonParam.put("username","hemin1997");
        jsonParam.put("password","123456");
        jsonParam.put("client_id","matrix");
        jsonParam.put("response_type","1");
        jsonParam.put("state","1");
        jsonParam.put("redirect_uri","");
        jsonParam.put("grant_type", "client_credentials");
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setHeader("Content-Type", "application/json");
        post.setEntity(entity);
        // 请求结束，返回结果
        try {
            HttpResponse res = httpClient.execute(post);
            // 如果服务器成功地返回响应
            String responseContent = null; // 响应内容
            HttpEntity httpEntity = res.getEntity();
            responseContent = EntityUtils.toString(httpEntity, "UTF-8");

            //System.out.println( responseContent);
            //JsonObject json = JsonParser.parse(responseContent);
            JSONObject json = JSONObject.parseObject(responseContent);
            // .getAsJsonObject();
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (json.get("errcode") != null) {
                    //resultStr = json.get("errcode").getAsString();
                } else {// 正常情况下
                    resultStr = json.get("access_token").toString();
                }
            }
        } catch (Exception e) {
            LOGGER.error("========={}",e);
        }
        // 关闭连接 ,释放资源
        httpClient.getConnectionManager().shutdown();
        return resultStr;
    }
/*
    public static String loginToken(String username,String pwd,String url) {
        System.setProperty("webdriver.firefox.bin", "D:/Program Files (x86)/Mozilla Firefox/firefox.exe");
        System.setProperty("webdriver.firefox.marionette","D:/Program Files (x86)/Mozilla Firefox/geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        //String url = "https://sso-prod.yysports.com/login?redirect_uri=http://wx.yysports.com/limitelottery/form.html&from=244000018&fromType=1&client_id=matrix&state=9508d129cfc84416814e1294495436d2";
        driver.get(url);
        WebDriverWait webDriverWait = new WebDriverWait(driver,20,5);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        ((FirefoxDriver) driver).findElementByName("username").sendKeys(username);
        ((FirefoxDriver) driver).findElementByName("pwd").sendKeys(pwd);
        ((FirefoxDriver) driver).findElementByName("button").click();

        *//*
        WebElement input = driver.findElement(By.xpath(".//*[@id='kw']"));
        CharSequence[] cs = new CharSequence[1];
        cs[0]="安居客";
        input.sendKeys(cs);

        WebElement btn = driver.findElement(By.xpath(".//*[@id='su']"));
        btn.click();
        // WebElement btn1 = driver.findElement(By.xpath(".//*[@id='w-75cn8k']/div/h2/a[1]"));
        //btn1.click();
        System.out.println("Page title is:"+driver.getTitle());*//*
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        Object token = ((FirefoxDriver) driver).executeScript("return sessionStorage.getItem(\"token\");","");
        System.out.printf(token + "");
        driver.close();
        return token + "";
    }*/
}
