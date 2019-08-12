package com.yysports.shoot.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yysports.shoot.config.RequestConfigInfo;
import com.yysports.shoot.mapper.ShootMapper;
import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.quartz.cacheDate.PayShareData;
import com.yysports.shoot.quartz.scheduler.QuartzScheduler;
import com.yysports.shoot.util.HttpClientUtil;
import com.yysports.shoot.util.ProxyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AddTaskExecSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTaskExecSchedule.class);

    @Autowired
    ShootMapper shootMapper;

    @Autowired
    QuartzScheduler quartzScheduler;

    @Autowired
    RequestConfigInfo requestConfigInfo;

    public void execPayProcess() {
        try {
            List<ShootBean> execList;
            synchronized (this) {
                PayShareData instance = PayShareData.getInstance();
                List<ShootBean> cache = instance.getShareList();
                if (cache == null || cache.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("execStatus", 1);
                    List<ShootBean> newCache = this.getNewCache(map);
                    if (newCache == null || newCache.isEmpty()) {
                        LOGGER.info("AddTaskExecSchedule#execPayProcess，已处理完库中数据");
                        String uuid = "b35f68d612fd11e998e60050569e1109";
                        this.pauseJob(uuid);
                        return;
                    } else {
                        instance.setShareList(newCache);
                    }
                    cache = instance.getShareList();
                } else {
                    LOGGER.info("cache的长度:{}", cache.size());
                }
                int defaultLen = 10;
                execList = new ArrayList<>(defaultLen);
                if (defaultLen >= cache.size()) {
                    defaultLen = cache.size();
                }
                for (int i = 0; i < defaultLen; i++) {
                    ShootBean shootBean = cache.get(i);
                    execList.add(shootBean);
                    cache.remove(i);
                    shootBean.setExecStatus(3);
                    shootMapper.updateShootInfo(shootBean);
                    if (defaultLen >= cache.size()) {
                        defaultLen = cache.size();
                    }
                }
            }
            if (execList != null || !execList.isEmpty()) {
               /* Map<String, List<ShootBean>> loginUserShoot = this.getLoginUserShoot(execList);
                this.modifyShoot(loginUserShoot);*/
               this.modifyShootList(execList);
            }
        } catch (Exception e) {
            LOGGER.error("异常信息", e);
        }
    }

    public Map<String,List<ShootBean>> getLoginUserShoot(List<ShootBean> execList){
        Map<String,List<ShootBean>> map = new HashMap<>();
        execList.forEach(shootBean -> {
            String jwt = shootBean.getJwt();
            List<ShootBean> list = null;
            if (map.get(jwt) != null) {
                list = map.get(jwt);
            } else {
                list = new ArrayList<>();
            }
            list.add(shootBean);
            map.put(jwt,list);
        });
        return map;
    }

    public String getDoLoginJson(String jwt) {
        String url = requestConfigInfo.getQueryOrAddUrl();
        String result = "";
        String isUseAgency = requestConfigInfo.getIsUseAgency();
        if(isUseAgency.equals("1")) {
            result = ProxyTest.doGet(url, jwt);
        } else {
            result = HttpClientUtil.doGet(url, jwt);
        }
        return result;
    }
    public String getTokenByUid(String userId,long uid) {
        String tokenUrl = requestConfigInfo.getTokenUrl();
        String url = tokenUrl + userId;
        String jwt = "";
        String result = HttpClientUtil.doGet(url,jwt);
        LOGGER.info("result=" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String newJwt = jsonObject.getString("jwt");
        String limitedUser = jsonObject.getString("limitedUser");
        Map<String,Object> map = new HashMap<>();
        map.put("id",uid);
        map.put("jwt",newJwt);
        map.put("limitedUser",limitedUser);
        shootMapper.updateUser(map);
        return newJwt;
    }


    public void modifyShootList(List<ShootBean> list) {
        String isUseAgency = requestConfigInfo.getIsUseAgency();
        String url = requestConfigInfo.getQueryOrAddUrl();
        for(int i = 0; i < list.size(); i++) {
            ShootBean shootBean = list.get(i);
            String jwt = shootBean.getJwt();
            long uid = shootBean.getId();
            String sid = shootBean.getSid();
            try{
                JSONArray jsonArray = null;
                String result = getDoLoginJson(jwt);
                try {
                    jsonArray = JSONObject.parseArray(result);
                    if(jsonArray.isEmpty()) {
                        String userId = shootBean.getUserId();
                        String tokenByUid = this.getTokenByUid(userId, uid);
                        result = getDoLoginJson(tokenByUid);
                        jsonArray = JSONObject.parseArray(result);
                    }
                } catch (Exception e) {
                    LOGGER.error("=========modifyShootList 获取登记列表异常!");
                    return;
                }

                ShootBean shootBeanNew = new ShootBean();
                shootBeanNew.setSid(sid);//shoot_info 主键
                shootBeanNew.setUid(uid);//users 主键
                String serialNo = shootBean.getSerialNo();
                shootBeanNew.setSerialNo(serialNo);//shoot_info 唯一序列号
                if (!jsonArray.isEmpty()) {
                    String itemModel = shootBean.getItemModel();//库中的鞋子款号
                    String shopName = shootBean.getShopName();
                    String shoesSize = shootBean.getShoesSize();
                    for(int j = 0; j < jsonArray.size(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        String itemModelReq = jsonObject.getString("itemModel");//yy鞋子款号
                        JSONObject json = new JSONObject();//存放请求的数据
                        if(itemModel.equals(itemModelReq)){
                            JSONArray postArray = new JSONArray();
                            //存放更新登记鞋子的信息
                            String activityItemId = jsonObject.getString("activityItemId");//返回的鞋子标识
                            json.put("activityItemId", activityItemId);
                            shootBeanNew.setActivityItemId(activityItemId);//添加鞋子标识
                            shootBeanNew.setItemModel(itemModelReq);//添加鞋子款号
                            JSONArray shoesSizes = jsonObject.getJSONArray("shoesSizes");
                            for (int n = 0; n < shoesSizes.size(); n++) {
                                if (shoesSize.equals(shoesSizes.get(n))) {
                                    json.put("shoesSize", shoesSize);
                                    shootBeanNew.setShoesSize(shoesSize);
                                    continue;
                                }
                            }
                            JSONArray activityShops = jsonObject.getJSONArray("activityShops");
                            for (int m = 0; m < activityShops.size(); m++) {
                                JSONObject shopJson = activityShops.getJSONObject(m);
                                String shopName1 = shopJson.getString("shopName");
                                String activityShopId = shopJson.getString("activityShopId");
                                if (shopName.equals(shopName1)) {
                                    json.put("activityShopId", activityShopId);
                                    shootBeanNew.setActivityShopId(activityShopId);
                                    shootBeanNew.setShopName(shopName1);
                                    continue;
                                }
                            }
                            if(json.containsKey("activityShopId")){
                                postArray.add(json);
                            } else {
                                LOGGER.error("店铺名称存在问题，登记店铺名称 shopName={}",shopName);
                            }
                            String itemName = jsonObject.getString("itemName");
                            shootBeanNew.setItemName(itemName);
                            String isSize = jsonObject.getString("isSize");
                            shootBeanNew.setIsSize(isSize);
                            if(!postArray.isEmpty()) {
                                String addedResult = "";
                                if(isUseAgency.equals("1")) {
                                    addedResult = ProxyTest.doPost(url,jwt,postArray.toJSONString());
                                } else {
                                    addedResult = HttpClientUtil.doPost(url, postArray.toJSONString(), jwt);
                                }

                                LOGGER.info("添加完鞋子返回的信息:{}", addedResult);
                                JSONArray addedArray = JSONArray.parseArray(addedResult);
                                if (!addedArray.isEmpty()) {
                                    for(int a = 0; a < addedArray.size(); a++) {
                                        JSONObject jsonResult = addedArray.getJSONObject(a);
                                        String uuid = jsonResult.getString("uuid");
                                        String activity = jsonResult.getString("activityItemId");
                                        shootBeanNew.setRegisterNo(uuid);
                                        shootBeanNew.setActivityItemId(activity);
                                        shootBeanNew.setState("1");
                                        shootBeanNew.setExecStatus(2);
                                        int count = shootMapper.addRecordShootBean(shootBeanNew);//添加登记记录
                                        if (count > 0) {
                                            shootMapper.updateShootInfo(shootBeanNew);//修改等级状态
                                        }
                                    }
                                    LOGGER.info("登记成功");
                                } else {
                                    LOGGER.info("登记失败，uid={},jwt={}",uid,jwt);
                                    shootBeanNew.setErrStat("2");
                                    shootMapper.updateUserInfo(shootBeanNew);
                                }
                            }

                        }
                    }
                } else {

                }
                shootMapper.updateShootInfo(shootBeanNew);//修改等级状态
            } catch (Exception e) {
                LOGGER.error("登记异常，uid={},errMsg={}", e.getMessage());
            }
        }
    }

    public synchronized List<ShootBean> getNewCache(Map<String, Object> map) {
        List<ShootBean> list = shootMapper.queryLoginShoot(map);
        List<ShootBean> newList = Collections.synchronizedList(new ArrayList<>());
        newList.addAll(list);
        return newList;
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
            shootMapper.updateTaskStatus(map);
        } catch (Exception e) {
            LOGGER.error("多线程任务异常!", e);
        }
    }

    public void modifyShoot(Map<String, List<ShootBean>> loginUserShoot) {
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsaW1pdGVkX2VkaXRpb25fbG90dGVyeSIsImlhdCI6MTU1NzUwMjA3MSwianRpIjoiMjczODEyMTMiLCJzdWIiOiIxMzI2MTM5Nzg0NCIsImxpbWl0ZWRVc2VyIjp0cnVlfQ.Ceohk3_qiKIqrXErfju61OvGvvh312n1wLFBjmr-9zQ";
        //String url = "http://wx.yysports.com/limitelottery/activity";
        for (Map.Entry<String, List<ShootBean>> entry : loginUserShoot.entrySet()) {
            String jwt = entry.getKey();
            List<ShootBean> value = entry.getValue();//存放当前用户下所有登记的鞋子数据
            //ShootBean user = shootMapper.queryShootBeanById(id);
            //String jwt = user.getJwt();
            String url = requestConfigInfo.getQueryOrAddUrl();
            String result = HttpClientUtil.doGet(url, jwt);
            JSONArray jsonArray = JSONObject.parseArray(result);
        /*if(jsonArray.isEmpty()) {
            result = "[{\"activityItemId\":859,\"itemName\":\"AIR JORDAN 4 RETRO (GS)\",\"itemModel\":\"408452116\",\"imageUrl\":\"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/af431d0c.jpg\",\"registerNo\":null,\"isSize\":\"1\",\"shoesSizes\":[\"3.5\",\"4\",\"4.5\",\"5\",\"5.5\",\"6\",\"7\"],\"activityShops\":[{\"activityShopId\":2762,\"shopName\":\"津滨江道NK JD\"},{\"activityShopId\":2763,\"shopName\":\"津和平路恒隆广场NK KL\"},{\"activityShopId\":2764,\"shopName\":\"津小白楼海信广场NK KL\"}]},{\"activityItemId\":860,\"itemName\":\"AIR JORDAN 3 RETRO TH SP\",\"itemModel\":\"CJ0939100\",\"imageUrl\":\"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/92a03455.jpg\",\"registerNo\":null,\"isSize\":\"1\",\"shoesSizes\":[\"7\",\"7.5\",\"8\",\"8.5\",\"9\",\"9.5\",\"10\",\"10.5\",\"11\",\"12\",\"13\"],\"activityShops\":[{\"activityShopId\":2765,\"shopName\":\"津滨江道NK JD\"},{\"activityShopId\":2766,\"shopName\":\"津和平路NK HOOPS\"}]},{\"activityItemId\":861,\"itemName\":\"AIR JORDAN XXXIII SE PF(106)\",\"itemModel\":\"CD9561106\",\"imageUrl\":\"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/0cbe173d.jpg\",\"registerNo\":null,\"isSize\":\"1\",\"shoesSizes\":[\"7\",\"7.5\",\"8\",\"8.5\",\"9\",\"9.5\",\"10\",\"10.5\",\"11\",\"12\"],\"activityShops\":[{\"activityShopId\":2767,\"shopName\":\"津滨江道NK JD\"},{\"activityShopId\":2768,\"shopName\":\"津滨江道班尼路NK BEACON\"},{\"activityShopId\":2769,\"shopName\":\"津和平路NK HOOPS\"},{\"activityShopId\":2770,\"shopName\":\"津和平路恒隆广场NK KL\"},{\"activityShopId\":2771,\"shopName\":\"津小白楼海信广场NK KL\"}]},{\"activityItemId\":862,\"itemName\":\"AIR JORDAN XXXIII SE PF(103)\",\"itemModel\":\"CD9561103\",\"imageUrl\":\"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/67a77877.jpg\",\"registerNo\":null,\"isSize\":\"1\",\"shoesSizes\":[\"7\",\"7.5\",\"8\",\"8.5\",\"9\",\"9.5\",\"10\",\"10.5\",\"11\",\"12\"],\"activityShops\":[{\"activityShopId\":2772,\"shopName\":\"津滨江道NK JD\"},{\"activityShopId\":2773,\"shopName\":\"津和平路NK HOOPS\"}]},{\"activityItemId\":863,\"itemName\":\"AIR JORDAN 14 RETRO\",\"itemModel\":\"487471100\",\"imageUrl\":\"https://e1xossfilehdd.blob.core.chinacloudapi.cn/maprod/upload/limitededitionshoes/app/custom/image/e0a61406.jpg\",\"registerNo\":null,\"isSize\":\"1\",\"shoesSizes\":[\"7\",\"7.5\",\"8\",\"8.5\",\"9\",\"9.5\",\"10\",\"10.5\",\"11\",\"12\"],\"activityShops\":[{\"activityShopId\":2774,\"shopName\":\"津滨江道NK JD\"}]}]";
            jsonArray = JSONObject.parseArray(result);
        }*/
            long uid = value.get(0).getId();
            try {
                JSONArray postArray = new JSONArray();
                if (!jsonArray.isEmpty()) {
                    for(int i = 0; i < value.size(); i++) {
                        ShootBean shootBean = value.get(i);
                        String sid = shootBean.getSid();
                        String itemModel = shootBean.getItemModel();
                        String shopName = shootBean.getShopName();
                        String shoesSize = shootBean.getShoesSize();
                        String serialNo = shootBean.getSerialNo();
                        for(int j = 0; j < jsonArray.size(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            String itemModelReq = jsonObject.getString("itemModel");//鞋子款号
                            JSONObject json = new JSONObject();//存放请求的数据
                            if(itemModel.equals(itemModelReq)){
                                ShootBean shootBeanNew = new ShootBean();//存放更新登记鞋子的信息
                                shootBeanNew.setUid(shootBean.getId());
                                shootBeanNew.setSerialNo(serialNo);
                                String activityItemId = jsonObject.getString("activityItemId");//返回的鞋子标识
                                json.put("activityItemId", activityItemId);
                                shootBeanNew.setActivityItemId(activityItemId);//添加鞋子标识
                                shootBeanNew.setItemModel(itemModelReq);//添加鞋子款号
                                JSONArray shoesSizes = jsonObject.getJSONArray("shoesSizes");
                                for (int n = 0; n < shoesSizes.size(); n++) {
                                    if (shoesSize.equals(shoesSizes.get(n))) {
                                        json.put("shoesSize", shoesSize);
                                        shootBeanNew.setShoesSize(shoesSize);
                                        continue;
                                    }
                                }
                                JSONArray activityShops = jsonObject.getJSONArray("activityShops");
                                for (int m = 0; m < activityShops.size(); m++) {
                                    JSONObject shopJson = activityShops.getJSONObject(m);
                                    String shopName1 = shopJson.getString("shopName");
                                    String activityShopId = shopJson.getString("activityShopId");
                                    if (shopName.equals(shopName1)) {
                                        json.put("activityShopId", activityShopId);
                                        shootBeanNew.setActivityShopId(activityShopId);
                                        shootBeanNew.setShopName(shopName1);
                                        continue;
                                    }
                                }
                                postArray.add(json);
                                String itemName = jsonObject.getString("itemName");
                                shootBeanNew.setItemName(itemName);
                                String isSize = jsonObject.getString("isSize");
                                shootBeanNew.setIsSize(isSize);
                                shootBeanNew.setSid(sid);
                                shootMapper.updateShootInfo(shootBeanNew);//修改等级状态
                                shootMapper.addRecordShootBean(shootBeanNew);//添加登记记录
                            }
                        }
                    }
                    if(postArray.size() > 0) {
                        String addedResult = HttpClientUtil.doPost(url, postArray.toJSONString(), jwt);
                        LOGGER.info("添加完鞋子返回的信息:{}", addedResult);
                        JSONArray addedArray = JSONArray.parseArray(addedResult);
                        if (addedArray.size() > 0) {
                            for(int a = 0; a < addedArray.size(); a++) {
                                ShootBean bean = new ShootBean();
                                JSONObject jsonObject1 = addedArray.getJSONObject(a);
                                String uuid = jsonObject1.getString("uuid");
                                String activityItemId = jsonObject1.getString("activityItemId");
                                bean.setRegisterNo(uuid);
                                bean.setActivityItemId(activityItemId);
                                bean.setUid(uid);
                                int count = shootMapper.updateRecordUUid(bean);
                                if (count > 0) {
                                    LOGGER.info("更新登记记录uuid成功!uuid={},activityItemId={},uid={}",uuid,activityItemId,uid);
                                } else {
                                    LOGGER.error("更新登记记录失败!uuid={},activityItemId={},uid={}",uuid,activityItemId,uid);
                                }
                            }
                            LOGGER.info("登记成功");
                        } else {
                            LOGGER.info("登记失败，uid={},jwt={}",uid,jwt);
                        }
                    }
                } else {
                    LOGGER.error("###!通过该jwt获得鞋子信息为0，jwt={},uid={}!###",jwt,uid);
                }
            } catch (Exception e) {
                ShootBean shootBean = value.get(0);
                ShootBean shootBeanNew = new ShootBean();//存放更新登记鞋子的信息
                String serialNo = shootBean.getSerialNo();
                String itemModel = shootBean.getItemModel();
                shootBeanNew.setUid(shootBean.getId());
                shootBeanNew.setSerialNo(serialNo);
                shootBeanNew.setItemModel(itemModel);//添加鞋子款号
                shootBeanNew.setResult(result);
                shootBeanNew.setUid(uid);
                shootBeanNew.setJwt(jwt);
            }



        }
    }


}
