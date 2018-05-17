package com.JWmes.project.monitor.operlog.service;

import com.JWmes.project.monitor.operlog.entity.OperLogEntity;

import java.util.List;

/**
 * 操作日志 服务层
 * 
 * @author zhong
 */
public interface OperLogService
{
    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    public void insertOperlog(OperLogEntity operLog);

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<OperLogEntity> selectOperLogList(OperLogEntity operLog);

    /**
     * 批量删除系统操作日志
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int batchDeleteOperLog(Long[] ids);

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public OperLogEntity selectOperLogById(Long operId);
}
