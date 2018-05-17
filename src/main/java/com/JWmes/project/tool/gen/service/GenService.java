package com.JWmes.project.tool.gen.service;

import com.JWmes.project.tool.gen.entity.TableInfoEntity;

import java.util.List;

/**
 * 代码生成 服务层
 * 
 * @author zhong
 */
public interface GenService
{
    /**
     * 查询ry数据库表信息
     * 
     * @param tableInfo 表信息
     * @return 数据库表列表
     */
    public List<TableInfoEntity> selectTableList(TableInfoEntity tableInfo);

    /**
     * 生成代码
     * 
     * @param tableName 表名称
     * @return 数据
     */
    public byte[] generatorCode(String tableName);
    
    /**
     * 批量生成代码
     * 
     * @param tableNames 表数组
     * @return 数据
     */
    public byte[] generatorCode(String[] tableNames);

}
