package com.JWmes.project.monitor.job.util;

import com.JWmes.common.constant.ScheduleConstants;
import com.JWmes.project.monitor.job.entity.JobEntity;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务工具类
 * 
 * @author zhong
 *
 */
public class ScheduleUtils
{
    private static final Logger log = LoggerFactory.getLogger(ScheduleUtils.class);
    
    private final static String JOB_NAME = "TASK_";

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId)
    {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId)
    {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId)
    {
        try
        {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, JobEntity jobEntity)
    {
        try
        {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(jobEntity.getJobId())).build();

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobEntity.getJobId())).withSchedule(scheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleConstants.JOB_PARAM_KEY, jobEntity);

            scheduler.scheduleJob(jobDetail, trigger);

            // 暂停任务
            if (jobEntity.getStatus() == ScheduleConstants.Status.PAUSE.getValue())
            {
                pauseJob(scheduler, jobEntity.getJobId());
            }
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, JobEntity jobEntity)
    {
        try
        {
            TriggerKey triggerKey = getTriggerKey(jobEntity.getJobId());

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());

            CronTrigger trigger = getCronTrigger(scheduler, jobEntity.getJobId());

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 参数
            trigger.getJobDataMap().put(ScheduleConstants.JOB_PARAM_KEY, jobEntity);

            scheduler.rescheduleJob(triggerKey, trigger);

            // 暂停任务
            if (jobEntity.getStatus() == ScheduleConstants.Status.PAUSE.getValue())
            {
                pauseJob(scheduler, jobEntity.getJobId());
            }

        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, JobEntity jobEntity)
    {
        try
        {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleConstants.JOB_PARAM_KEY, jobEntity);

            scheduler.triggerJob(getJobKey(jobEntity.getJobId()), dataMap);
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId)
    {
        try
        {
            scheduler.pauseJob(getJobKey(jobId));
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId)
    {
        try
        {
            scheduler.resumeJob(getJobKey(jobId));
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId)
    {
        try
        {
            scheduler.deleteJob(getJobKey(jobId));
        }
        catch (SchedulerException e)
        {
            log.error(e.getMessage());
        }
    }
}
