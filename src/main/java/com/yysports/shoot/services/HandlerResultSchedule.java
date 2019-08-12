package com.yysports.shoot.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yysports.shoot.config.RequestConfigInfo;
import com.yysports.shoot.mapper.ShootMapper;
import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.quartz.cacheDate.PayShareData;
import com.yysports.shoot.quartz.cacheDate.QueryResultData;
import com.yysports.shoot.quartz.scheduler.QuartzScheduler;
import com.yysports.shoot.util.HttpClientUtil;
import com.yysports.shoot.util.ProxyTest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HandlerResultSchedule {

    Logger LOGGER = LoggerFactory.getLogger(PayShareData.class);

    @Autowired
    ShootMapper shootMapper;

    @Autowired
    QuartzScheduler quartzScheduler;

    @Autowired
    RequestConfigInfo requestConfigInfo;

    public Map<String, List<ShootBean>> getLoginUserShoot(List<ShootBean> execList) {
        Map<String, List<ShootBean>> map = new HashMap<>();
        for(ShootBean shootBean : execList) {
            String jwt = shootBean.getJwt();
            List<ShootBean> list = null;
            if (map.get(jwt) != null) {
                list = map.get(jwt);
            } else {
                list = new ArrayList<>();
            }
            list.add(shootBean);
            map.put(jwt, list);
        }
        /*execList.forEach(shootBean -> {
            String jwt = shootBean.getJwt();
            List<ShootBean> list = null;
            if (map.get(jwt) != null) {
                list = map.get(jwt);
            } else {
                list = new ArrayList<>();
            }
            list.add(shootBean);
            map.put(jwt, list);
        });*/
        return map;
    }

    public String getTokenByUid(String userId, long uid) {
        String tokenUrl = requestConfigInfo.getTokenUrl();
        String url = tokenUrl + userId;
        String jwt = "";
        String result = HttpClientUtil.doGet(url, jwt);
        LOGGER.info("result=" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String newJwt = jsonObject.getString("jwt");
        String limitedUser = jsonObject.getString("limitedUser");
        Map<String, Object> map = new HashMap<>();
        map.put("id", uid);
        map.put("jwt", newJwt);
        map.put("limitedUser", limitedUser);
        shootMapper.updateUser(map);
        return newJwt;
    }

    public void resultHandler() {
        QueryResultData instance = QueryResultData.getInstance();
        List<ShootBean> execList = null;
        LOGGER.info(Thread.currentThread().getName());
        //Map<String, List<ShootBean>> map = new HashMap<>();
        synchronized (this) {
            //map.clear();
            //Map<String, List<ShootBean>> shootMap1 = instance.getShootMap();
            List<ShootBean> shareList = instance.getShareList();
            if (null == shareList || shareList.isEmpty()) {
                List<ShootBean> shootBeans = shootMapper.queryUserList();
                if (null == shootBeans || shootBeans.isEmpty()) {
                    String uuid = "b35f68d612fd11e998e60050569e1110";
                    this.pauseJob(uuid);
                    return;
                }
//                shootMap1 = this.getLoginUserShoot(shootBeans);
                instance.setShareList(shootBeans);
                shareList = instance.getShareList();
            }
            int defaultLen = 50;
            if(defaultLen > shareList.size()) {
                defaultLen = shareList.size();
            }
            execList = Collections.synchronizedList(new ArrayList<>(defaultLen));
            for(int i = 0; i < defaultLen; i++) {
                ShootBean shootBean = shareList.get(i);
                execList.add(shootBean);
                shareList.remove(i);
                shootMapper.updateUserById(shootBean);
                if(defaultLen > shareList.size()) {
                    defaultLen = shareList.size();
                }
            }
            if (execList == null || execList.isEmpty()) {
                LOGGER.error(".............无登记列表..............");
                return;
            }
            /*Map<String, List<ShootBean>> shootMap = instance.getShootMap();
            Set<Map.Entry<String, List<ShootBean>>> entries = shootMap.entrySet();
            int defaultLength = 50;
            Iterator<Map.Entry<String, List<ShootBean>>> iterator = entries.iterator();
            boolean flag = true;
            int start = 0;
            while (iterator.hasNext() && flag) {
                Map.Entry<String, List<ShootBean>> next = iterator.next();
                String key = next.getKey();
                List<ShootBean> value = next.getValue();
                map.put(key, value);
                shootMap.remove(key);
                ShootBean shootBean = value.get(0);
                shootMapper.updateUserById(shootBean);
                start++;
                if (start == defaultLength) {
                    flag = false;
                }
            }*/

        }

        for(ShootBean shootBean: execList) {
            String jwt = shootBean.getJwt();
            String url = requestConfigInfo.getAlreadyUrl();
            String isUseAgency = requestConfigInfo.getIsUseAgency();
            String json = "";
            if (isUseAgency.equals("1")) {
                json = ProxyTest.doGet(url, jwt);
            } else {
                json = HttpClientUtil.doGet(url, jwt);
            }
            JSONArray jsonArray = JSONArray.parseArray(json);
            if (jsonArray.isEmpty()) {
                long id = shootBean.getId();
                String userId = shootBean.getUserId();
                String tokenByUid = this.getTokenByUid(userId, id);
                if (isUseAgency.equals("1")) {
                    json = ProxyTest.doGet(url, tokenByUid);
                } else {
                    json = HttpClientUtil.doGet(url, tokenByUid);
                }
            }
            jsonArray = JSONArray.parseArray(json);
            if (jsonArray.isEmpty()) {
                LOGGER.info("无法同去user token uis={}");
                return;
            } else {
                String activityItemId1 = shootBean.getActivityItemId();
                long pid = shootBean.getPid();
                String registerNo1 = shootBean.getRegisterNo();
                jsonArray = JSONArray.parseArray(json);
                if (!jsonArray.isEmpty()) {
                    for(int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //{"activityShops":[{"activityShopId":3501,"shopName":"京朝阳世贸天阶胜道NK JD-YY"}],"itemName":"北京AIR JORDAN 14 RETRO SE","activityItemId":1081,"itemModel":"BQ3685-706","imageUrl":"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/745c5cd3.jpg","shoesSize":"","state":"2","registerNo":"DFPYG5207716"}
                        String activityItemId = jsonObject.getString("activityItemId");
                        String state = jsonObject.getString("state");
                        String registerNo = jsonObject.getString("registerNo");
                        String itemName = jsonObject.getString("itemName");
                        String itemModel = jsonObject.getString("itemModel");
                        /*if(StringUtils.isBlank(activityItemId1) || StringUtils.isBlank(registerNo)) {
                            LOGGER.error("activityItemId1={} or registerNo={}", activityItemId, registerNo);
                            return;
                        }*/
                        if (activityItemId1.equals(activityItemId) && registerNo1.equals(registerNo)) {
                            ShootBean b = new ShootBean();
                            b.setPid(pid);
                            b.setState(state);
                            b.setRegisterNo(registerNo);
                            int i1 = shootMapper.updateRecordList(b);
                            if (i1 ==0 ) {
                                shootBean.setState(state);
                                shootBean.setRegisterNo(registerNo);
                                shootBean.setItemName(itemName);
                                shootBean.setActivityItemId(activityItemId);
                                shootBean.setItemModel(itemModel);
                                shootMapper.addNotReq(shootBean);
                                shootMapper.updateNotReq(shootBean);
                            }
                        }
                    }
                }
                /*for(ShootBean shoot : list) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String activityItemId = jsonObject.getString("activityItemId");
                        String state = jsonObject.getString("state");
                        String myActivityItemId = shoot.getActivityItemId();
                        if(myActivityItemId.equals(activityItemId)) {
                            ShootBean b = new ShootBean();
                            b.setUid(shoot.getId());
                            b.setActivityItemId(myActivityItemId);
                            b.setState(state);
                            shootMapper.updateRecordList(b);
                        }
                    }

                }*/
            }



        }
        /*if (map.isEmpty()) {
            LOGGER.info("数据已经同步完成!");
            return;
        } else {
            for (Map.Entry<String, List<ShootBean>> entity : map.entrySet()) {
                String jwt = entity.getKey();
                List<ShootBean> list = entity.getValue();
                String url = requestConfigInfo.getAlreadyUrl();
                String isUseAgency = requestConfigInfo.getIsUseAgency();
                String json = "";
                if (isUseAgency.equals("1")) {
                    json = ProxyTest.doGet(url, jwt);
                } else {
                    json = HttpClientUtil.doGet(url, jwt);
                }
                JSONArray jsonArray = JSONArray.parseArray(json);
                boolean flag = false;
                if (jsonArray.isEmpty()) {
                    flag = true;
                } else {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.containsKey("activityItemId")) {
                        flag = true;
                    }
                }
                if (flag) {
                    ShootBean shootBean = list.get(0);
                    long id = shootBean.getId();
                    String userId = shootBean.getUserId();
                    String tokenByUid = this.getTokenByUid(userId, id);
                    if (isUseAgency.equals("1")) {
                        json = ProxyTest.doGet(url, tokenByUid);
                    } else {
                        json = HttpClientUtil.doGet(url, tokenByUid);
                    }
                }
                jsonArray = JSONArray.parseArray(json);
                for(ShootBean shoot : list) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String activityItemId = jsonObject.getString("activityItemId");
                        String state = jsonObject.getString("state");
                        String myActivityItemId = shoot.getActivityItemId();
                        if(myActivityItemId.equals(activityItemId)) {
                            ShootBean b = new ShootBean();
                            b.setUid(shoot.getId());
                            b.setActivityItemId(myActivityItemId);
                            b.setState(state);
                            shootMapper.updateRecordList(b);
                        }
                    }

                }

            }

        }*/
    }

    public void pauseJob(String uuid) {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        JobTask jobTask = shootMapper.queryJobTask(map);
        String jobName = jobTask.getJobName();
        String jobGroup = jobTask.getJobGroup();
        try {
            quartzScheduler.pauseJob(jobName, jobGroup);
            String status = quartzScheduler.getJobStatus(jobName, jobGroup);
            map.put("jobStatus", status);
            LOGGER.info("=====线程的状态====={}", status);
            shootMapper.updateTaskStatus(map);
        } catch (Exception e) {
            LOGGER.error("多线程任务异常!", e);
        }
    }
}
