package com.JWmes.project.monitor.logininfor.mapper;



import com.JWmes.project.monitor.logininfor.entity.LogininforEntity;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author zhong
 */
public interface LogininforMapper
{
    /**
     * 新增系统登录日志
     * 
     * @param LogininforEntity 访问日志对象
     */
    public void insertLogininforEntity(LogininforEntity LogininforEntity);

    /**
     * 查询系统登录日志集合
     * 
     * @param LogininforEntity 访问日志对象
     * @return 登录记录集合
     */
    public List<LogininforEntity> selectLogininforEntityList(LogininforEntity LogininforEntity);

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    public int batchDeleteLogininforEntity(Long[] ids);
}
