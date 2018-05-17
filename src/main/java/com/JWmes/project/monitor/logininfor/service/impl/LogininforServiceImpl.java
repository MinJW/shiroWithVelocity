package com.JWmes.project.monitor.logininfor.service.impl;


import com.JWmes.project.monitor.logininfor.entity.LogininforEntity;
import com.JWmes.project.monitor.logininfor.mapper.LogininforMapper;
import com.JWmes.project.monitor.logininfor.service.LogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author zhong
 */
@Service("LogininforService")
public class LogininforServiceImpl implements LogininforService
{

    @Autowired
    private LogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     * 
     * @param LogininforEntity 访问日志对象
     */
    @Override
    public void insertLogininforEntity(LogininforEntity LogininforEntity)
    {
        logininforMapper.insertLogininforEntity(LogininforEntity);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param LogininforEntity 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<LogininforEntity> selectLogininforEntityList(LogininforEntity LogininforEntity)
    {
        return logininforMapper.selectLogininforEntityList(LogininforEntity);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int batchDeleteLogininforEntity(Long[] ids)
    {
        return logininforMapper.batchDeleteLogininforEntity(ids);
    }
}
