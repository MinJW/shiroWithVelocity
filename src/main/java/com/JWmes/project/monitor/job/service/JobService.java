package com.JWmes.project.monitor.job.service;

import com.JWmes.project.monitor.job.entity.JobEntity;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 * 
 * @author zhong
 */
public interface JobService
{

    /**
     * 获取quartz调度器的计划任务
     * 
     * @param jobEntity 调度信息
     * @return 调度任务集合
     */
    public List<JobEntity> selectJobList(JobEntity jobEntity);

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    public JobEntity selectJobById(Long jobId);

    /**
     * 暂停任务
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int pauseJob(JobEntity jobEntity);

    /**
     * 恢复任务
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int resumeJob(JobEntity jobEntity);

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int deleteJob(JobEntity jobEntity);

    /**
     * 批量删除调度信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public void batchDeleteJob(Long[] ids);

    /**
     * 任务调度状态修改
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int changeStatus(JobEntity jobEntity);

    /**
     * 立即运行任务
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int triggerJob(JobEntity jobEntity);

    /**
     * 新增任务表达式
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int addJobCron(JobEntity jobEntity);

    /**
     * 更新任务的时间表达式
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int updateJobCron(JobEntity jobEntity);

    /**
     * 保存任务的时间表达式
     * 
     * @param jobEntity 调度信息
     * @return 结果
     */
    public int saveJobCron(JobEntity jobEntity);
}
