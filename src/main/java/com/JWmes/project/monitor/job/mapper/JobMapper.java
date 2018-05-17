package com.JWmes.project.monitor.job.mapper;

import com.JWmes.project.monitor.job.entity.JobEntity;

import java.util.List;

/**
 * 调度任务信息 数据层
 * 
 * @author zhong
 */
public interface JobMapper
{

    /**
     * 查询调度任务日志集合
     * 
     * @param jobEntity 调度信息
     * @return 操作日志集合
     */
    public List<JobEntity> selectJobList(JobEntity jobEntity);

    /**
     * 查询所有调度任务
     * 
     * @return 调度任务列表
     */
    public List<JobEntity> selectJobAll();

    /**
     * 通过调度ID查询调度任务信息
     * 
     * @param jobId 调度ID
     * @return 角色对象信息
     */
    public JobEntity selectJobById(Long jobId);

    /**
     * 通过调度ID删除调度任务信息
     * 
     * @param jobId 调度ID
     * @return 结果
     */
    public int deleteJobById(JobEntity jobEntity);

    /**
     * 批量删除调度任务信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int batchDeleteJob(Long[] ids);

    /**
     * 修改调度任务信息
     * 
     * @param jobEntity 调度任务信息
     * @return 结果
     */
    public int updateJob(JobEntity jobEntity);

    /**
     * 新增调度任务信息
     * 
     * @param jobEntity 调度任务信息
     * @return 结果
     */
    public int insertJob(JobEntity jobEntity);

}
