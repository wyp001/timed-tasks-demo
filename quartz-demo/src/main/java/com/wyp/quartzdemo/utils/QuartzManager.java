package com.wyp.quartzdemo.utils;

import com.wyp.quartzdemo.entity.Task;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 计划任务管理
 * @author 小卖铺的老爷爷
 * @date 2018年6月17日
 * @website www.laoyeye.net
 */
@Service
public class QuartzManager {
     private static Logger log = LoggerFactory.getLogger(QuartzManager.class);
    @Autowired
    private Scheduler scheduler;

    /**
     * 添加任务
     *
     */    
    @SuppressWarnings("unchecked")
    public void addJob(Task task) {
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类

            Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(task.getBeanClass()).newInstance()
                    .getClass());
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(task.getJobName(), task.getJobGroup())// 任务名称和组构成任务key
                    .build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())// 触发器key
                    .startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有计划中的任务列表
     * 
     * @return
     * @throws SchedulerException
     */
    public List<Task> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<Task> jobList = new ArrayList<Task>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Task job = new Task();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     * 
     * @return
     * @throws SchedulerException
     */
    public List<Task> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<Task> jobList = new ArrayList<Task>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            Task job = new Task();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     * 
     * @param task
     * @throws SchedulerException
     */
    public void pauseJob(Task task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     * 
     * @param task
     * @throws SchedulerException
     */
    public void resumeJob(Task task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     * 
     * @param task
     * @throws SchedulerException
     */
    public void deleteJob(Task task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     * 
     * @param task
     * @throws SchedulerException
     */
    public void runJobNow(Task task) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     * 
     * @param task
     * @throws SchedulerException
     */
    public void updateJobCron(Task task) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}