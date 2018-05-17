package com.JWmes.project.monitor.job.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.JWmes.common.constant.ScheduleConstants;
import com.JWmes.common.utils.spring.SpringUtils;
import com.JWmes.project.monitor.job.entity.JobEntity;
import com.JWmes.project.monitor.job.entity.JobLogEntity;
import com.JWmes.project.monitor.job.service.JobLogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务
 * 
 * @author zhong
 *
 */
public class ScheduleJob extends QuartzJobBean
{
    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        JobEntity jobEntity = (JobEntity) context.getMergedJobDataMap().get(ScheduleConstants.JOB_PARAM_KEY);

        JobLogService jobLogService = (JobLogService) SpringUtils.getBean(JobLogService.class);

        JobLogEntity jobLog = new JobLogEntity();
        jobLog.setJobName(jobEntity.getJobName());
        jobLog.setJobGroup(jobEntity.getJobGroup());
        jobLog.setMethodName(jobEntity.getMethodName());
        jobLog.setParams(jobEntity.getParams());
        jobLog.setCreateTime(new Date());

        long startTime = System.currentTimeMillis();

        try
        {
            // 执行任务
            log.info("任务开始执行 - 名称：{} 方法：{}", jobEntity.getJobName(), jobEntity.getMethodName());
            ScheduleRunnable task = new ScheduleRunnable(jobEntity.getJobName(), jobEntity.getMethodName(), jobEntity.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            // 任务状态 0：成功 1：失败
            jobLog.setIsException(0);
            jobLog.setJobMessage(jobEntity.getJobName() + " 总共耗时：" + times + "毫秒");

            log.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", jobEntity.getJobName(), times);
        }
        catch (Exception e)
        {
            log.info("任务执行失败 - 名称：{} 方法：{}", jobEntity.getJobName(), jobEntity.getMethodName());
            log.error("任务执行异常  - ：", e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setJobMessage(jobEntity.getJobName() + " 总共耗时：" + times + "毫秒");
            // 任务状态 0：成功 1：失败
            jobLog.setIsException(1);
            jobLog.setExceptionInfo(e.toString());
        }
        finally
        {
            jobLogService.addJobLog(jobLog);
        }
    }
}
