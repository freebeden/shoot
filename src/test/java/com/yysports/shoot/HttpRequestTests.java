package com.yysports.shoot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yysports.shoot.quartz.job.SchedulerQuartzJob;
import com.yysports.shoot.util.HttpClientKeepSession;
import com.yysports.shoot.util.HttpClientUtil;
import com.yysports.shoot.util.HttpConnectionUtil;
import com.yysports.shoot.util.ProxyTest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
*/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URI;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestTests.class);

    @Test
    public void checkTicket(){
        String url = "https://fpcy.shenzhen.chinatax.gov.cn/WebQuery/vatQuery?callback=jQuery110209706438138234976_1558503114910&key1=4403181130&key2=26323324&key3=20181112&key4=401.26&fplx=01&yzm=qssd&yzmSj=2019-05-22+13%3A10%3A20&index=ac278d363cf85b0513e8fb79f4ded1fa&area=4403&publickey=E4185176051ED43F80F482760F663033&_=1558503114922";
        String result = HttpClientUtil.doGet(url,"");
        LOGGER.info("返回的消息:{}",result);
    }

    @Test
    public void doLogin1() {
//        JSONObject json = new JSONObject();
//        json.put("username","hemin1997");
//        json.put("password","123456");
//        json.put("client_id","matrix");
//        json.put("response_type","1");
//        json.put("state","2");
//        json.put("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
        //String url = "https://sso-prod.yysports.com/login?redirect_uri=http://wx.yysports.com/limitelottery/form.html&from=244000018&fromType=1&client_id=matrix&state=9508d129cfc84416814e1294495436d2";
        //String url = "https://sso-prod.yysports.com/api/member/pousheng/account/captcha-check?username=hemin1997&token=''";
//        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/captcher";
        //String url = "http://wx.yysports.com/api/member/pousheng/account/login";
        //String url = "http://wx.yysports.com/api/member/pousheng/account/login";
        String url = "https://sso-prod.yysports.com/oauth/authorize?client_id=matrix&redirect_uri=http%3A%2F%2Fwx.yysports.com%2Flimitelottery%2Fform.html&response_type=code&state=ddd8e9e572e84425a43b8fabc2d39dd0";
        String result = HttpClientUtil.doGet(url,"");
        LOGGER.info("返回的消息:{}",result);
    }



    @Test
    public void coik(){
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("username","helloweenvsfei");
        LOGGER.info("key:" + cookie.getName() + "  value:" + cookie.getValue());
    }

    @Test
    public void addUser() throws Exception{
        //[key:tssoid  value:dd2f4894-1f28-463d-86ec-ef4b17ba6dea]
        HttpClientKeepSession session = new HttpClientKeepSession();
        //用户登陆
        JSONObject json = new JSONObject();
        json.put("username","hemin1997");
        json.put("password","123456");
        json.put("client_id","matrix");
        json.put("response_type","1");
        json.put("state","2");
        json.put("redirect_uri","");
        CloseableHttpResponse response = HttpClientKeepSession.post("https://sso-prod.yysports.com/api/member/pousheng/account/login",json.toJSONString());
        //httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        /*CloseableHttpClient http = null;
        CloseableHttpResponse re = null;
        HttpEntity en = null;
        String res = null;
        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        http = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject json = new JSONObject();
        json.put("username","hemin1997");
        json.put("password","123456");
        json.put("client_id","matrix");
        json.put("response_type","1");
        json.put("state","2");
        json.put("redirect_uri","");
        StringEntity entity = new StringEntity(json.toString(), "UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
        HttpClient httpClient = new DefaultHttpClient();
        org.apache.http.HttpResponse httpresponse = httpClient.execute(post);
        HttpEntity entity1 = httpresponse.getEntity();
        String body = EntityUtils.toString(entity1, "UTF-8");

        HttpGet httpget = new HttpGet(url);
        re = http.execute(httpget);
        en = re.getEntity();
        res = EntityUtils.toString(en, "utf-8");
        LOGGER.info("res=============" + body);
        http.close();
//        Pattern p = Pattern.compile("name="csrf_token" value="(.+?)"/>");
        Pattern p = Pattern.compile("csrf_token");
        Matcher m = p.matcher(res);*/





    }

    /**
     * 添加鞋子
     */
    @Test
    public void queryLogShoot() {
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1OTQzODY5MSwianRpIjoiMjg4MTU1NzEiLCJzdWIiOiIxMzExNzY0ODI1NiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.cO6_zW7Sdcwn1Z1yE75ggUZTIw5ktA3gD8k3ymnlBXQ";
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU2MTU5OTQyNywianRpIjoiMjgwMzMwOTQiLCJzdWIiOiIxMzUxMzQ2NDk4MyIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.o8RLZMZqIMwxylYzBAbnzW6E7GIz33sTQb3zj5aJFks";
        //String url = "http://wx.yysports.com/limitelottery/activity/registitems";
        //String url = "http://wx.yysports.com/limitelottery/activity";
        //String url = "http://wx.yysports.com/limitelotterybeijing/activity?activityId=926";
        //String url = "http://wx.yysports.com/limitelottery/activity/registitems";
        String url = "http://wx.yysports.com/limitelotterybeijing/activity/registitems";
        String s = HttpClientUtil.doGet(url, jwt);
        LOGGER.info("返回的消息:{}",s);
        /*JSONArray jsonArray = JSONObject.parseArray(s);
        if(jsonArray.size() > 0) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String activityItemId = jsonObject.get("activityItemId").toString();
            LOGGER.info("activityItemId={}",activityItemId);
            JSONArray shoesSizes = jsonObject.getJSONArray("shoesSizes");
            LOGGER.info("shoesSizes={}",shoesSizes);
            JSONArray activityShops = jsonObject.getJSONArray("activityShops");
            LOGGER.info("activityShops={}",activityShops);


            JSONObject json = new JSONObject();

        }*/

    }

    @Test
    public void addShoot() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzUwMjA3MSwianRpIjoiMjczODEyMTMiLCJzdWIiOiIxMzI2MTM5Nzg0NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.Ceohk3_qiKIqrXErfju61OvGvvh312n1wLFBjmr-9zQ";
        String url = "http://wx.yysports.com/limitelottery/activity";
        JSONObject json = new JSONObject();
        json.put("activityItemId","1022");
        json.put("shoesSize","4");
        json.put("activityShopId","3317");
        JSONArray array = new JSONArray();
        array.add(json);
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzM2NzM4OCwianRpIjoiMjgyOTIzMjYiLCJzdWIiOiIxMzkzNzE2MzUxNCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.ZOvSj5yeVa3Y2GYFAz0wSnGDyiaVOy1vLurtEIJ_8_4";
        String result = HttpClientUtil.doPost(url,array.toJSONString(),jwt);
        LOGGER.info("返回的消息:{}",result);

        /*JSONObject json = new JSONObject();
        json.put("activityItemId","");
        json.put("shoesSize","");
        json.put("activityShopId","");
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzM2NzM4OCwianRpIjoiMjgyOTIzMjYiLCJzdWIiOiIxMzkzNzE2MzUxNCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.ZOvSj5yeVa3Y2GYFAz0wSnGDyiaVOy1vLurtEIJ_8_4";
        String result = HttpClientUtil.doPost(url,json.toJSONString(),jwt);
        LOGGER.info("返回的消息:{}",result);*/
    }







    @Test
    public void queryShootList() {
        //获取登记的url
        String url = "http://wx.yysports.com/limitelottery/activity/registitems";
        //
        //String url = "http://wx.yysports.com/limitelottery/activity";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzM5NzE2MiwianRpIjoiMjI4OTI2NjEiLCJzdWIiOiIxMzI2MTg2NTQwNiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.Mq4y4L0eDKK5WWm_rfU_-DzSJPXsW-kuus_EnpT33c0";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzUwMTg5NiwianRpIjoiMjc4MDc3ODMiLCJzdWIiOiIxMzQ2MzUyNTY2NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.qlAFwaKS81JqoZMpBLiiRmCe3Zrg75BlsRedWJq3mfo";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzUwMjA3MSwianRpIjoiMjczODEyMTMiLCJzdWIiOiIxMzI2MTM5Nzg0NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.Ceohk3_qiKIqrXErfju61OvGvvh312n1wLFBjmr-9zQ";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1Nzg5MTI5MCwianRpIjoiMjg3MjUzODQiLCJzdWIiOiIxMzI2MTM3NDI0MyJ9._qwF58TNFsPUE3oaPyLTL17lofVKlTnpSvSrrUriPcg";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzUwMjA3MSwianRpIjoiMjczODEyMTMiLCJzdWIiOiIxMzI2MTM5Nzg0NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.Ceohk3_qiKIqrXErfju61OvGvvh312n1wLFBjmr-9zQ";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1ODUzOTY5NCwianRpIjoiMjI4OTI2NjEiLCJzdWIiOiIxMzI2MTg2NTQwNiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.mXNmbdAICgDuKKXAjOA5CkPQ7euJtth-4uAvlt7YQKY";
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1ODQxMTIzNywianRpIjoiMjgyNzk3NTQiLCJzdWIiOiIxNTAyODM1NzA2OCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.yeLqeZw-0TbEZ31cj_vsmEGu-BWjaZnM09cX3mErzY4";
        String s = HttpClientUtil.doGet(url, jwt);
        LOGGER.info(s);
    }

    @Test
    public void testString(){
        String str = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzI5NjU2OSwianRpIjoiMjgyOTIzMjYiLCJzdWIiOiIxMzkzNzE2MzUxNCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.FoKHFXq5y3r2SwJPHUWS8NYJlhqNdsoNrUrKmwU2RAA";
        String str2 = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzMwNjY4NCwianRpIjoiMjgyOTIzNTAiLCJzdWIiOiIxNzg2MDg5MDM3NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.smzFiwXV5J55BMGdSvfyDFcBuQnPECiPugOyPP0IvGE";
        Map<String,Object> nap = new HashMap<>();
        nap.put(str,123);
        nap.put(str2,456);
        LOGGER.info("str=" + nap.get(str));
        LOGGER.info("str2=" + nap.get(str2));
    }

    //通过uid获得用户的jwt
    @Test
    public void loginByUid(){
        //key:tssoid  value:fac12565-86f0-4925-8087-6ba256b254bc
        String uid = "22892661";
        String url = "http://wx.yysports.com/limitelottery/regist/checkyyuser?uid=" + uid;
        String result = HttpClientUtil.doGet(url,"");
        LOGGER.info("result=" + result);
    }

    @Test
    public void loginByCode(){
        //result={"yysportLoginUrl":"https://sso-prod.yysports.com/login","domain":"http://wx.yysports.com/limitelottery"}
        //key:tssoid  value:fac12565-86f0-4925-8087-6ba256b254bc
        String code = "fkS059";
        //String url = "http://wx.yysports.com/limitelottery/regist/checkssologin?code=" + code + "&redirecturl=";
        //String url = "http://wx.yysports.com/limitelottery/regist/url";
        String url = "http://wx.yysports.com/limitelottery/regist/checkssologin?code=j6n28u&redirecturl=form.html";
        String result = HttpClientUtil.doGet(url,"");
        LOGGER.info("result=" + result);
    }

    @Test
    public void testClass() throws Exception{
        Class<SchedulerQuartzJob> schedulerQuartzJobClass = SchedulerQuartzJob.class;
        Class<?> aClass = Class.forName("com.yysports.shoot.quartz.job.SchedulerQuartzJob");
        LOGGER.info(aClass.getCanonicalName());
        LOGGER.info(aClass.getName());
        LOGGER.info(aClass.getSimpleName());
        LOGGER.info(aClass.getTypeName());

        LOGGER.info("--------------------------------");

        LOGGER.info(schedulerQuartzJobClass.getCanonicalName());
        LOGGER.info(schedulerQuartzJobClass.getName());
        LOGGER.info(schedulerQuartzJobClass.getSimpleName());
        LOGGER.info(schedulerQuartzJobClass.getTypeName());
    }


    @Test
    public void getTokenByUid() {
        String uid = "22892661";
        String url = "http://wx.yysports.com/limitelottery/regist/checkyyuser?uid=" + uid;
        String jwt = "";
        String result = HttpClientUtil.doGet(url,jwt);
        LOGGER.info("result=" + result);
    }

    @Test
    public void dooPost_deprecated() {
        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        DefaultHttpClient httpClient = null;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            JSONObject json = new JSONObject();
            json.put("username","hemin1997");
            json.put("password","123456");
            json.put("client_id","matrix");
            json.put("response_type","code");
            json.put("state","d7d396c410574f52b9b993cca3f4ce74");
            json.put("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/json;charset=utf-8");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            post.setHeader("x-requested-with", "XMLHttpRequest");
            HttpResponse response = httpClient.execute(post);
            LOGGER.info("getStatusCode==" + response.getStatusLine().getStatusCode());
            String JSESSIONID = null;
            String cookie_user = null;

            HttpEntity entity1 = response.getEntity();
            String body = EntityUtils.toString(entity1, "UTF-8");
            JSONObject urlJson = JSONObject.parseObject(body);
            String redirect = urlJson.getString("redirect");
            HttpGet httpget = new HttpGet(redirect);
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
            HttpEntity entity2 = httpresponse.getEntity();
            body = EntityUtils.toString(entity2, "utf-8");
            //获得Cookies
            CookieStore cookieStore = httpClient.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                //遍历Cookies
                LOGGER.info("cookies.get(i)==" + cookies.get(i));
                LOGGER.info("cookiename=="+cookies.get(i).getName());
                LOGGER.info("cookieValue=="+cookies.get(i).getValue());
                LOGGER.info("Domain=="+cookies.get(i).getDomain());
                LOGGER.info("Path=="+cookies.get(i).getPath());
                LOGGER.info("Version=="+cookies.get(i).getVersion());

                if (cookies.get(i).getName().equals("JSESSIONID")) {
                    JSESSIONID = cookies.get(i).getValue();
                }
                if (cookies.get(i).getName().equals("cookie_user")) {
                    cookie_user = cookies.get(i).getValue();
                }
            }
            if (cookie_user != null) {
                result = JSESSIONID;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 通过用户名和密码登陆系统
     */
    @Test
    public void doLogin() throws Exception{
        JSONObject json = new JSONObject();
        json.put("username","hemin1997");
        json.put("password","123456");
        json.put("client_id","matrix");
        json.put("response_type","code");
        json.put("state","d7d396c410574f52b9b993cca3f4ce74");
        json.put("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
        //json.put("access_token","");
        //json.put("captcha_token","");
        /*json.put("captcha_answer","");
        json.put("redirect_uri","");*/
        //json.put("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
        //String url = "https://sso-prod.yysports.com/login?redirect_uri=http://wx.yysports.com/limitelottery/form.html&from=244000018&fromType=1&client_id=matrix&state=9508d129cfc84416814e1294495436d2";
        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        //String url = "http://wx.yysports.com/api/member/pousheng/account/login";
        //String result = HttpClientKeepSession.doPost(url,json.toJSONString(),"");
        String result = HttpClientUtil.doPost(url,json.toJSONString(),"");
        LOGGER.info("返回的消息1:{}",result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String urls = jsonObject.get("redirect").toString();
        //{"redirect":"https://sso-prod.yysports.com/oauth/authorize?client_id=matrix&redirect_uri=http%3A%2F%2Fwx.yysports.com%2Flimitelottery%2Fform.html&response_type=code&state=d7d396c410574f52b9b993cca3f4ce74"}
        CloseableHttpResponse closeableHttpResponse = HttpClientKeepSession.get(urls);
    }



    /*@Test
    public void testFormParam(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username","hemin1997");
        formData.add("password","123456");
        formData.add("client_id","matrix");
        formData.add("response_type","code");
        formData.add("state","d7d396c410574f52b9b993cca3f4ce74");
        formData.add("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        *//*Mono<String> stringMono = *//*WebClient.create().post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve().bodyToMono(String.class);
        //LOGGER.info("result:{}",stringMono.block());
    }*/

    @Test
    public void doget(){
        String targetUrl = "http://wx.yysports.com/limitelottery/activity/registitems";
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1OTA1NDYxMiwianRpIjoiMjI4OTI2NjEiLCJzdWIiOiIxMzI2MTg2NTQwNiIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.bcAN1LrGQ7m8tXTfox3nkOeuLptU1gpF5csU34O8BnA";
        String s = ProxyTest.doGet(targetUrl, jwt);
        LOGGER.info(s);
    }

    @Test
    public void addLogin() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username","hemin1997");
        json.put("password","123456");
        json.put("client_id","matrix");
        json.put("response_type","code");
        json.put("state","d7d396c410574f52b9b993cca3f4ce74");
        json.put("redirect_uri","http://wx.yysports.com/limitelottery/form.html");
        String url = "https://sso-prod.yysports.com/api/member/pousheng/account/login";
        HttpConnectionUtil.httpPostWithJson(json,url);
    }
}
