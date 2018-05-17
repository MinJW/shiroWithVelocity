package com.JWmes.project.system.role.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.JWmes.common.constant.UserConstants;
import com.JWmes.common.utils.StringUtils;
import com.JWmes.common.utils.security.ShiroUtils;
import com.JWmes.project.system.role.entity.RoleEntity;
import com.JWmes.project.system.role.entity.RoleMenuEntity;
import com.JWmes.project.system.role.mapper.RoleMapper;
import com.JWmes.project.system.role.mapper.RoleMenuMapper;
import com.JWmes.project.system.role.service.RoleService;
import com.JWmes.project.system.user.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 角色 业务层处理
 * 
 * @author zhong
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService
{

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<RoleEntity> selectRoleList(RoleEntity role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Long userId)
    {
        List<RoleEntity> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (RoleEntity perm : perms)
        {
            if (StringUtils.isNotNull(perms))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<RoleEntity> selectRolesByUserId(Long userId)
    {
        List<RoleEntity> userRoles = roleMapper.selectRolesByUserId(userId);
        List<RoleEntity> roles = roleMapper.selectRolesAll();
        for (RoleEntity role : roles)
        {
            for (RoleEntity userRole : userRoles)
            {
                if (role.getRoleId() == userRole.getRoleId())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<RoleEntity> selectRoleAll()
    {
        return roleMapper.selectRolesAll();
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public RoleEntity selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleById(Long roleId)
    {
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int batchDeleteRole(Long[] ids)
    {
        roleMenuMapper.deleteRoleMenu(ids);
        return roleMapper.batchDeleteRole(ids);
    }

    /**
     * 保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int saveRole(RoleEntity role)
    {
        Long roleId = role.getRoleId();
        if (StringUtils.isNotNull(roleId))
        {
            role.setUpdateBy(ShiroUtils.getLoginName());
            // 修改角色信息
            roleMapper.updateRole(role);
            // 删除角色与菜单关联
            roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        }
        else
        {
            role.setCreateBy(ShiroUtils.getLoginName());
            // 新增角色信息
            roleMapper.insertRole(role);
        }
        return insertRoleMenu(role);
    }

    /**
     * 新增角色菜单信息
     *
     */
    public int insertRoleMenu(RoleEntity role)
    {
        int rows = 1;
        // 新增用户与角色管理
        List<RoleMenuEntity> list = new ArrayList<RoleMenuEntity>();
        for (Long menuId : role.getMenuIds())
        {
            RoleMenuEntity rm = new RoleMenuEntity();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(RoleEntity role)
    {
        Long roleId = role.getRoleId();
        RoleEntity info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && StringUtils.isNotNull(info.getRoleId()) && info.getRoleId() != roleId)
        {
            return UserConstants.NAME_NOT_UNIQUE;
        }
        return UserConstants.NAME_UNIQUE;
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int selectCountUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.selectCountUserRoleByRoleId(roleId);
    }

}
