package com.yysports.shoot.quartz.job;

import com.yysports.shoot.services.HandlerUserJwtSchedule;
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
public class SchedulerQuartzJwt implements Job {
    Logger LOGGER = LoggerFactory.getLogger(SchedulerQuartzJwt.class);

    @Autowired
    private HandlerUserJwtSchedule handlerUserJwtSchedule;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        LOGGER.info("更新jwt任务开始执行！name={}",Thread.currentThread().getName());
        handlerUserJwtSchedule.syncUserJwt();
    }



}
