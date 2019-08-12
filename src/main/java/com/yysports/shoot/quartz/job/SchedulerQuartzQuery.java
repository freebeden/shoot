package com.yysports.shoot.quartz.job;

import com.yysports.shoot.services.HandlerResultSchedule;
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
public class SchedulerQuartzQuery implements Job {
    Logger LOGGER = LoggerFactory.getLogger(SchedulerQuartzQuery.class);

    @Autowired
    private HandlerResultSchedule handlerResultSchedule;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //LOGGER.info("更新状态任务开始执行！name={}",Thread.currentThread().getName());
        handlerResultSchedule.resultHandler();
    }



}
