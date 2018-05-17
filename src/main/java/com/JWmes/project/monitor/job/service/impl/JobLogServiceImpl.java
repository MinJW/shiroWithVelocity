package com.JWmes.project.monitor.job.service.impl;

import java.util.List;

import com.JWmes.project.monitor.job.entity.JobLogEntity;
import com.JWmes.project.monitor.job.mapper.JobLogMapper;
import com.JWmes.project.monitor.job.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度日志信息 服务层
 * 
 * @author zhong
 */
@Service("jobLogService")
public class JobLogServiceImpl implements JobLogService
{

    @Autowired
    private JobLogMapper jobLogMapper;

    /**
     * 获取quartz调度器日志的计划任务
     * 
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<JobLogEntity> selectJobLogList(JobLogEntity jobLog)
    {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     * 

     * @return 调度任务日志对象信息
     */
    @Override
    public JobLogEntity selectJobLogById(Long jobLogId)
    {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * 新增任务日志
     * 
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(JobLogEntity jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * 批量删除调度日志信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int batchDeleteJoblog(Long[] ids)
    {
        return jobLogMapper.batchDeleteJobLog(ids);
    }

    /**
     * 删除任务日志
     * 
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return jobLogMapper.deleteJobLogById(jobId);
    }

}
