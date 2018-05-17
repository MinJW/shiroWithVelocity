package com.JWmes.project.system.role.mapper;

import com.JWmes.project.system.role.entity.RoleEntity;

import java.util.List;

/**
 * 角色表 数据层
 * 
 * @author zhong
 */
public interface RoleMapper
{

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<RoleEntity> selectRoleList(RoleEntity role);

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<RoleEntity> selectRolesByUserId(Long userId);

    /**
     * 查询角色列表
     * 
     * @return 角色列表
     */
    public List<RoleEntity> selectRolesAll();

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public RoleEntity selectRoleById(Long roleId);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int batchDeleteRole(Long[] ids);

    /**
     * 修改角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(RoleEntity role);

    /**
     * 新增角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(RoleEntity role);

    /**
     * 校验角色名称是否唯一
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    public RoleEntity checkRoleNameUnique(String roleName);

}
