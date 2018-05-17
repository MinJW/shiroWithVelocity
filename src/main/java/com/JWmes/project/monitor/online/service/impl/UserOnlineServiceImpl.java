package com.JWmes.project.monitor.online.service.impl;

import java.util.Date;
import java.util.List;

import com.JWmes.common.utils.DateUtils;
import com.JWmes.framework.shiro.session.OnlineSessionMapper;
import com.JWmes.project.monitor.online.entity.UserOnlineEntity;
import com.JWmes.project.monitor.online.mapper.UserOnlineMapper;
import com.JWmes.project.monitor.online.service.UserOnlineService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 在线用户 服务层处理
 * 
 * @author zhong
 */
@Service("userOnlineService")
public class UserOnlineServiceImpl implements UserOnlineService
{
    @Autowired
    private UserOnlineMapper userOnlineMapper;

    @Autowired
    private OnlineSessionMapper onlineSessionMapper;

    /**
     * 通过会话序号查询信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public UserOnlineEntity selectOnlineById(String sessionId)
    {
        return userOnlineMapper.selectOnlineById(sessionId);
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public void deleteOnlineById(String sessionId)
    {
        UserOnlineEntity userOnline = selectOnlineById(sessionId);
        if (userOnline != null)
        {
            userOnlineMapper.deleteOnlineById(sessionId);
        }
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    @Override
    public void batchDeleteOnline(List<String> sessions)
    {
        for (String sessionId : sessions)
        {
            UserOnlineEntity userOnline = selectOnlineById(sessionId);
            if (userOnline != null)
            {
                userOnlineMapper.deleteOnlineById(sessionId);
            }
        }
    }

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     */
    @Override
    public void saveOnline(UserOnlineEntity online)
    {
        userOnlineMapper.saveOnline(online);
    }

    /**
     * 查询会话集合
     * 

     */
    @Override
    public List<UserOnlineEntity> selectUserOnlineList(UserOnlineEntity userOnline)
    {
        return userOnlineMapper.selectUserOnlineList(userOnline);
    }

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    @Override
    public void forceLogout(String sessionId)
    {
        Session session = onlineSessionMapper.readSession(sessionId);
        if (session == null)
        {
            return;
        }
        session.setTimeout(1000);
        userOnlineMapper.deleteOnlineById(sessionId);
    }

    /**
     * 查询会话集合
     * 

     */
    @Override
    public List<UserOnlineEntity> selectOnlineByExpired(Date expiredDate)
    {
        String lastAccessTime = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", expiredDate);
        return userOnlineMapper.selectOnlineByExpired(lastAccessTime);
    }
}
