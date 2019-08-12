package com.yysports.shoot;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class HttpRequestTest2 {

    public static String doPost(String url, String json,String jwt) {
        String body = null;
        HttpEntity entity1 = null;
        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(json, "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/json");
            if(StringUtils.isNotBlank(jwt)) {
                post.setHeader("Authorization", "Bearer " + jwt);
            }
            org.apache.http.HttpResponse httpresponse = httpClient.execute(post);
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

    @Test
    public void checkCookie(){
        // 登陆 Url
        String loginUrl = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        // 需登陆后访问的 Url
        String dataUrl = "http://wx.yysports.com/limitelottery/form.html&from=244000018&fromType=1&client_id=matrix&state=a868e1c2e44143efba716fa9d18667ef";
        HttpClient httpClient = new HttpClient();

        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);

        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = {
                new NameValuePair("username", "hemin1997"),
                new NameValuePair("password", "123456"),
                new NameValuePair("client_id", "matrix"),
                new NameValuePair("response_type", "1"),
                new NameValuePair("state", "2"),
                new NameValuePair("redirect_uri", dataUrl)
        };
        postMethod.setRequestBody(data);
        try {
            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            int statusCode=httpClient.executeMethod(postMethod);

            // 获得登陆后的 Cookie
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
                System.out.println("cookies = "+c.toString());
            }
            if(statusCode==302){//重定向到新的URL
                System.out.println("模拟登录成功");
                // 进行登陆后的操作
                GetMethod getMethod = new GetMethod(dataUrl);
                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
                getMethod.setRequestHeader("cookie", tmpcookies.toString());
                // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
                // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
                postMethod.setRequestHeader("Referer", "http://passport.mop.com/");
                postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
                httpClient.executeMethod(getMethod);
                // 打印出返回数据，检验一下是否成功
                String text = getMethod.getResponseBodyAsString();
                System.out.println(text);
            }
            else {
                System.out.println("登录失败");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
