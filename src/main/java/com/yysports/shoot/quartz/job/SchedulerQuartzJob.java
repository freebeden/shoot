package com.yysports.shoot.quartz.job;

import com.yysports.shoot.services.AddTaskExecSchedule;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:zhouyou
 * @Description: 实际处理任务的
 * @Date:Created in 2018/12/28z
 */
// @DisallowConcurrentExecution
public class SchedulerQuartzJob implements Job {
    Logger LOGGER = LoggerFactory.getLogger(SchedulerQuartzJob.class);

    @Autowired
    private AddTaskExecSchedule addTaskExecSchedule;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
         //LOGGER.info("登记任务开始执行！name={}",Thread.currentThread().getName());
        addTaskExecSchedule.execPayProcess();
    }

}
