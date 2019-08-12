package com.yysports.shoot.services;

import com.alibaba.fastjson.JSONObject;
import com.yysports.shoot.config.RequestConfigInfo;
import com.yysports.shoot.mapper.ShootMapper;
import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.quartz.cacheDate.QueryUserData;
import com.yysports.shoot.quartz.scheduler.QuartzScheduler;
import com.yysports.shoot.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HandlerUserJwtSchedule {

    Logger LOGGER = LoggerFactory.getLogger(HandlerUserJwtSchedule.class);

    @Autowired
    QuartzScheduler quartzScheduler;
    @Autowired
    ShootMapper shootMapper;
    @Autowired
    RequestConfigInfo requestConfigInfo;

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
            LOGGER.info("=====线程的状态====={}",status);
            shootMapper.updateTaskStatus(map);
        } catch (Exception e) {
            LOGGER.error("多线程任务异常!", e);
        }
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



    public void syncUserJwt(){
        QueryUserData userData = QueryUserData.getInstall();
        List<ShootBean> myCache;
        synchronized (this) {
            List<ShootBean> userList = userData.getUserList();
            if(userList == null || userList.isEmpty()) {
                Map<String,Object> map = new HashMap<>();
                map.put("jwt_stat","1");
                List<ShootBean> shootBeans = shootMapper.queryAllUsers(map);
                userData.setUserList(shootBeans);
                if(userList == null || userList.isEmpty()) {
                    String uuid = "b35f68d612fd11e998e60050569e1111";
                    this.pauseJob(uuid);
                    return;
                }
            }
            List<ShootBean> cache = userData.getUserList();
            int defaultLen = 10;
            if(defaultLen > cache.size()) {
                defaultLen = cache.size();
            }
            myCache = Collections.synchronizedList(new ArrayList<>(10));
            for(int i = 0; i < defaultLen; i++) {
                ShootBean shootBean = cache.get(i);
                myCache.add(shootBean);
                cache.remove(i);
                shootBean.setJwtStat("2");
                shootMapper.updateUserState(shootBean);
                if(defaultLen > cache.size()) {
                    defaultLen = cache.size();
                }
            }
        }
        if(myCache == null || myCache.isEmpty()) {
            LOGGER.info("已经将myCache执行完成!");
            return;
        } else {
            myCache.forEach(biean -> {
                long id = biean.getId();
                String userId = biean.getUserId();
                this.getTokenByUid(userId,id);
            });
        }
    }
}
