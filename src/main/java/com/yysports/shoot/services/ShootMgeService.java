package com.yysports.shoot.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yysports.shoot.config.RequestConfigInfo;
import com.yysports.shoot.mapper.ShootMapper;
import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.quartz.scheduler.QuartzScheduler;
import com.yysports.shoot.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShootMgeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootMgeService.class);

    private static final int DEFAULT_LEN = 100;

    @Autowired
    ShootMapper shootMapper;

    @Autowired
    QuartzScheduler quartzScheduler;

    @Autowired
    RequestConfigInfo requestConfigInfo;

    /**
     * 验证环境搭建
     */
    public ShootBean loginByAccount(int id) {
        return shootMapper.loginByAccount(id);
    }

    public List<ShootBean> queryUserList() {
        return shootMapper.queryUserList();
    }

    public List<JobTask> queryJobTaskList(Map<String, Object> map) {
        List<JobTask> jobTasks = shootMapper.queryJobTaskList(map);
        return jobTasks;
    }

    public void updateTaskStatus(Map<String, Object> map) {
        shootMapper.updateTaskStatus(map);
    }

    public JobTask queryJobTask(String uuid) {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        return shootMapper.queryJobTask(map);
    }

    public String modifyJobTask(String status,String uuid) {
        JobTask jobTask = this.queryJobTask(uuid);
        String jobGroup = jobTask.getJobGroup();
        String jobName = jobTask.getJobName();
        int count = 0;
        try {
            if ("NORMAL".equals(status)) {
                // 启动
                quartzScheduler.startAllJob();
            } else if ("PAUSED".equals(status)) {
                // 暂停
                quartzScheduler.pauseJob(jobName, jobGroup);
            } else if ("NONE".equals(status)) {
                // 停止
                quartzScheduler.deleteJob(jobName, jobGroup);
            } else if ("RESUME".equals(status)) {
                // 重启
                quartzScheduler.resumeJob(jobName, jobGroup);
            } else {
                LOGGER.error("------------无此状态!-------------");
            }
            String jobStatusNow = quartzScheduler.getJobStatus(jobName, jobGroup);
            Map<String, Object> map = new HashMap<>();
            map.put("jobStatus", jobStatusNow);
            //String uuid = "b35f68d612fd11e998e60050569e1109";
            map.put("uuid", uuid);
            count = shootMapper.updateTaskStatus(map);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        String retMsg = "";
        if (count > 0) {
            retMsg = "跟新成功!";
        } else {
            retMsg = "跟新失败!";
        }
        return retMsg;
    }

    public int updateUserById(ShootBean shootBean) {
        return shootMapper.updateUserById(shootBean);
    }

    public void insertShootInfo(List<ShootBean> list) {
        int length = list.size();
        int defaultLength = DEFAULT_LEN;
        int size = length % defaultLength == 0 ? length / defaultLength : length / defaultLength + 1;
        List<ShootBean> childList = null;
        for (int i = 0; i < size; i++) {
            if (defaultLength * (i + 1) < length) {
                childList = list.subList(defaultLength * i, defaultLength * (i + 1));
            } else {
                childList = list.subList(defaultLength * i, length);
            }
            shootMapper.batchInsert(childList);
        }
    }


    public void queryLoginResult() {
        LOGGER.info("查询登记状态开始");
        List<ShootBean> shootBeans = shootMapper.queryResultBean();
        shootBeans.forEach(shoot -> {
            String jwt = shoot.getJwt();
            String serialNo = shoot.getSerialNo();
            String url = requestConfigInfo.getAlreadyUrl();
            String json = HttpClientUtil.doGet(url, jwt);
            JSONArray jsonArray = JSONArray.parseArray(json);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String activityItemId = jsonObject.getString("activityItemId");
                String state = jsonObject.getString("state");
                String registerNo = jsonObject.getString("registerNo");
                ShootBean bean = new ShootBean();
                bean.setActivityItemId(activityItemId);
                bean.setSerialNo(serialNo);
                bean.setState(state);
                bean.setRegisterNo(registerNo);
                shootMapper.updateRecordList(bean);
            }
        });
        LOGGER.info("查询登记状态结束");
    }

    public List<ShootBean> queryShootLoginBean(Map<String,Object> map) {
        return shootMapper.queryShootLoginBean(map);
    }


    public List<ShootBean> getShootBeanList(ShootBean bean){
        return shootMapper.getShootBeanList(bean);
    }
}
