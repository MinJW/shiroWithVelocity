package com.JWmes.project.system.user.entity;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author zhong
 */
public class UserRoleEntity
{
    /** 用户ID */
    private Long userId;
    /** 角色ID */
    private Long roleId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @Override
    public String toString()
    {
        return "UserRoleEntity [userId=" + userId + ", roleId=" + roleId + "]";
    }

}
