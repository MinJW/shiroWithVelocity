package com.JWmes.project.monitor.operlog.service.impl;

import java.util.List;

import com.JWmes.project.monitor.operlog.entity.OperLogEntity;
import com.JWmes.project.monitor.operlog.mapper.OperLogMapper;
import com.JWmes.project.monitor.operlog.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务层处理
 * 
 * @author zhong
 */
@Service("operLogService")
public class OperLogServiceImpl implements OperLogService
{
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(OperLogEntity operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<OperLogEntity> selectOperLogList(OperLogEntity operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int batchDeleteOperLog(Long[] ids)
    {
        return operLogMapper.batchDeleteOperLog(ids);
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public OperLogEntity selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }
}
