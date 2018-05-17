package com.JWmes.project.system.user.entity;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author zhong
 */
public class UserPostEntity
{
    /** 用户ID */
    private Long userId;
    /** 岗位ID */
    private Long postId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    @Override
    public String toString()
    {
        return "UserPostEntity [userId=" + userId + ", postId=" + postId + "]";
    }

}
