package com.yysports.shoot.quartz.listener;

import com.yysports.shoot.config.RequestConfigInfo;
import com.yysports.shoot.mapper.ShootMapper;
import com.yysports.shoot.quartz.scheduler.QuartzScheduler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartQuartzJobListener.class);

    @Autowired
    private QuartzScheduler quartzScheduler;
    @Autowired
    private RequestConfigInfo requestConfigInfo;
    @Autowired
    private ShootMapper shootMapper;

    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            quartzScheduler.startAllJob();
            LOGGER.info("任务已经启动...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @Bean public Scheduler scheduler() throws SchedulerException{ Properties
     * p = new Properties(); String threadCount = payConfig.getThreadCount();
     * LOGGER.info("获得的线程数量============{} ",threadCount);
     * p.setProperty("org.quartz.threadPool.threadCount",
     * String.valueOf(threadCount));
     * p.setProperty("org.quartz.threadPool.class",
     * "org.quartz.simpl.SimpleThreadPool"); SchedulerFactory factory = new
     * StdSchedulerFactory(p); return factory.getScheduler(); }
     */

    /**
     * @Author:john
     * @Description: 将任务工厂添加到spring上下文管理
     * @params:applicationContext
     * @Date:4:09 2019/1/2
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * @Author:john
     * @Description:初始注入scheduler
     * @params:jobFactory
     * @Date:4:10 2019/1/2
     */
    @Bean
    public Scheduler scheduler(JobFactory jobFactory) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setJobFactory(jobFactory);
        Properties p = new Properties();
        String threadCount = shootMapper.queryThreadCount();
        LOGGER.info("线程数量={}",threadCount);
        //String threadCount = requestConfigInfo.getThreadCount();
        p.setProperty("org.quartz.threadPool.threadCount", threadCount);
        p.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        factory.setQuartzProperties(p);
        factory.afterPropertiesSet();
        return factory.getScheduler();
    }

}
