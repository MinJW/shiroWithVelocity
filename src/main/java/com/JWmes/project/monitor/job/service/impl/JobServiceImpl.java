package com.JWmes.project.monitor.job.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import com.JWmes.common.constant.ScheduleConstants;
import com.JWmes.common.utils.StringUtils;
import com.JWmes.common.utils.security.ShiroUtils;
import com.JWmes.project.monitor.job.entity.JobEntity;
import com.JWmes.project.monitor.job.mapper.JobMapper;
import com.JWmes.project.monitor.job.service.JobService;
import com.JWmes.project.monitor.job.util.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 定时任务调度信息 服务层
 * 
 * @author zhong
 */
@Service("jobService")
public class JobServiceImpl implements JobService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init()
    {
        List<JobEntity> jobEntityList = jobMapper.selectJobAll();
        for (JobEntity jobEntity : jobEntityList)
        {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, jobEntity.getJobId());
            // 如果不存在，则创建
            if (cronTrigger == null)
            {
                ScheduleUtils.createScheduleJob(scheduler, jobEntity);
            }
            else
            {
                ScheduleUtils.updateScheduleJob(scheduler, jobEntity);
            }
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param jobEntity 调度信息
     * @return
     */
    @Override
    public List<JobEntity> selectJobList(JobEntity jobEntity)
    {
        return jobMapper.selectJobList(jobEntity);
    }

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public JobEntity selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int pauseJob(JobEntity jobEntity)
    {
        jobEntity.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        jobEntity.setUpdateBy(ShiroUtils.getLoginName());
        int rows = jobMapper.updateJob(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.pauseJob(scheduler, jobEntity.getJobId());
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int resumeJob(JobEntity jobEntity)
    {
        jobEntity.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        jobEntity.setUpdateBy(ShiroUtils.getLoginName());
        int rows = jobMapper.updateJob(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.resumeJob(scheduler, jobEntity.getJobId());
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int deleteJob(JobEntity jobEntity)
    {
        int rows = jobMapper.deleteJobById(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.deleteScheduleJob(scheduler, jobEntity.getJobId());
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public void batchDeleteJob(Long[] ids)
    {
        for (Long jobId : ids)
        {
            JobEntity jobEntity = jobMapper.selectJobById(jobId);
            deleteJob(jobEntity);
        }
    }

    /**
     * 任务调度状态修改
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int changeStatus(JobEntity jobEntity)
    {
        int rows = 0;
        int status = jobEntity.getStatus();
        if (status == 0)
        {
            rows = resumeJob(jobEntity);
        }
        else if (status == 1)
        {
            rows = pauseJob(jobEntity);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int triggerJob(JobEntity jobEntity)
    {
        int rows = jobMapper.updateJob(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.run(scheduler, jobEntity);
        }
        return rows;
    }

    /**
     * 新增任务
     * 
     * @param jobEntity 调度信息 调度信息
     */
    @Override
    public int addJobCron(JobEntity jobEntity)
    {
        jobEntity.setCreateBy(ShiroUtils.getLoginName());
        jobEntity.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.createScheduleJob(scheduler, jobEntity);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int updateJobCron(JobEntity jobEntity)
    {
        int rows = jobMapper.updateJob(jobEntity);
        if (rows > 0)
        {
            ScheduleUtils.updateScheduleJob(scheduler, jobEntity);
        }
        return rows;
    }

    /**
     * 保存任务的时间表达式
     * 
     * @param jobEntity 调度信息
     */
    @Override
    public int saveJobCron(JobEntity jobEntity)
    {
        Long jobId = jobEntity.getJobId();
        int rows = 0;
        if (StringUtils.isNotNull(jobId))
        {
            rows = updateJobCron(jobEntity);
        }
        else
        {
            rows = addJobCron(jobEntity);
        }
        return rows;
    }

}
