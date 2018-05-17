package com.JWmes.project.system.dept.mapper;

import com.JWmes.project.system.dept.entity.DeptEntity;

import java.util.List;

/**
 * 部门管理 数据层
 * 
 * @author zhong
 */
public interface DeptMapper
{
    /**
     * 查询部门人数
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int selectDeptCount(DeptEntity dept);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 查询部门管理集合
     * 
     * @return 所有部门信息
     */
    public List<DeptEntity> selectDeptAll();

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 新增部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(DeptEntity dept);

    /**
     * 修改部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(DeptEntity dept);

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    public DeptEntity selectDeptById(Long deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param deptName 部门名称
     * @return 结果
     */
    public DeptEntity checkDeptNameUnique(String deptName);
}
