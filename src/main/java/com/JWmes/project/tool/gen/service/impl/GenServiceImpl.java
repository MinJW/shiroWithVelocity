package com.JWmes.project.tool.gen.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.JWmes.common.exception.base.BaseException;
import com.JWmes.common.utils.StringUtils;
import com.JWmes.framework.config.GenConfig;
import com.JWmes.project.tool.gen.entity.ColumnInfoEntity;
import com.JWmes.project.tool.gen.entity.TableInfoEntity;
import com.JWmes.project.tool.gen.mapper.GenMapper;
import com.JWmes.project.tool.gen.service.GenService;
import com.JWmes.project.tool.gen.util.GenUtils;
import com.JWmes.project.tool.gen.util.VelocityInitializer;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代码生成 服务层处理
 * 
 * @author zhong
 */
@Service("genService")
public class GenServiceImpl implements GenService
{
    @Autowired
    private GenMapper genDao;

    /**
     * 查询ry数据库表信息
     * 
     * @param tableInfo 表信息
     * @return 数据库表列表
     */
    @Override
    public List<TableInfoEntity> selectTableList(TableInfoEntity tableInfo)
    {
        return genDao.selectTableList(tableInfo);
    }

    /**
     * 生成代码
     * 
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] generatorCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 查询表信息
        TableInfoEntity table = genDao.selectTableByName(tableName);
        // 查询列信息
        List<ColumnInfoEntity> columns = genDao.selectTableColumnsByName(tableName);
        // 生成代码
        generatorCode(table, columns, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 批量生成代码
     * 
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] generatorCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            // 查询表信息
            TableInfoEntity table = genDao.selectTableByName(tableName);
            // 查询列信息
            List<ColumnInfoEntity> columns = genDao.selectTableColumnsByName(tableName);
            // 生成代码
            generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码
     */
    public void generatorCode(TableInfoEntity table, List<ColumnInfoEntity> columns, ZipOutputStream zip)
    {
        // 表名转换成Java属性名
        String className =GenUtils.tableToJava(table.getTableName());
        table.setClassName(className);
        table.setClassname(StringUtils.uncapitalize(className));
        // 列信息
        table.setColumns(GenUtils.transColums(columns));
        // 设置主键
        table.setPrimaryKey(table.getColumnsLast());

        VelocityInitializer.initVelocity();

        String packageName = GenConfig.getPackageName();
        String moduleName = GenUtils.getModuleName(packageName);

        VelocityContext context = GenUtils.getVelocityContext(table);

        // 获取模板列表
        List<String> templates = GenUtils.getTemplates();
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try
            {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(GenUtils.getFileName(template, table, moduleName)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            }
            catch (IOException e)
            {
                throw new BaseException("渲染模板失败，表名：" + table.getTableName(), e.getMessage());
            }
        }
    }
}
