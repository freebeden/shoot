package com.yysports.shoot.quartz.scheduler;

import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.quartz.job.SchedulerQuartzJob;
import com.yysports.shoot.services.ShootMgeService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class QuartzScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzScheduler.class);
    @Autowired
    ShootMgeService shootMgeService;
    // 任务调度
    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void startAllJob() throws Exception {
        /*
         * List<TFcsJobTask> tFcsJobTasks = null; Map<String, Object> map = new
         * HashMap<>(); if (null != tripartShareProfitService) { tFcsJobTasks =
         * tripartShareProfitService.queryTFcsJobTaskList(map); } List<String>
         * list = new ArrayList<>(); if (null == tFcsJobTasks) {
         * startDefaultJob(scheduler); LOGGER.error("-----执行默认的方法!-----"); }
         * else { for (TFcsJobTask task : tFcsJobTasks) { startJob(scheduler,
         * task); list.add(task.getUuid()); } }
         */
        Map<String, Object> myParam = new HashMap<>();
        //myParam.put("uuid", "b35f68d612fd11e998e60050569e1109");
        List<JobTask> jobTasks = shootMgeService.queryJobTaskList(myParam);
        for(JobTask tFcsJobTast : jobTasks) {
            startJob(scheduler, tFcsJobTast);
            //String jobName = tFcsJobTast.getJobName();
            //String jobGroup = tFcsJobTast.getJobGroup();
            //String info = this.getJobStatus(jobName, jobGroup);
            //LOGGER.info("开始前状态:------" + info);
            scheduler.start();
            pauseAllJob();
            //info = this.getJobStatus(jobName, jobGroup);
            //LOGGER.info("结束后状态:------" + info);
            myParam.put("jobStatus", "PAUSED");
            shootMgeService.updateTaskStatus(myParam);
        }
    }

    /**
     * @throws SchedulerException
     * @Author:john
     * @Description:获取任务信息
     * @params: name, group
     * @Date:20:03 2018/12/29
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * @throws SchedulerException
     * @Author:john
     * @Description:获取任务状态
     * @params: name, group
     * @Date:20:03 2018/12/29
     */
    public String getJobStatus(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        return scheduler.getTriggerState(triggerKey).name();
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        LOGGER.info("name={},group={},jobKey={}",name,group,jobKey);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 启动任务
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void startJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.start();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }

    /**
     * 启动任务
     *
     * @param scheduler
     * @param task
     * @throws SchedulerException
     */
    private void startJob(Scheduler scheduler, JobTask task) throws Exception {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        LOGGER.info("jobName:" + task.getJobName() + ",jobGroup:" + task.getJobGroup() + ",cronExpression:" + task
                .getCronExpression());
        String jobPath = task.getJobPath();
        Class<Job> aClass = (Class<Job>)Class.forName(jobPath);
        JobDetail jobDetail = JobBuilder.newJob(aClass)
                .withIdentity(task.getJobName(), task.getJobGroup()).build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    /**
     * 启动默认任务
     *
     * @param scheduler
     * @throws SchedulerException
     */
    private void startDefaultJob(Scheduler scheduler) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity("shootJobName", "分润任务组").build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("分润任务调度", "分润任务组")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}