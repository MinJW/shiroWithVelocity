package com.JWmes.framework.shiro.session;

import javax.servlet.http.HttpServletRequest;

import com.JWmes.common.utils.IpUtils;
import com.JWmes.common.utils.ServletUtils;
import com.JWmes.common.utils.StringUtils;
import com.JWmes.project.monitor.online.entity.OnlineSessionEntity;
import com.JWmes.project.monitor.online.entity.UserOnlineEntity;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 自定义sessionFactory会话
 * 
 * @author zhong
 */
@Component
public class OnlineSessionFactory implements SessionFactory
{
    public Session createSession(UserOnlineEntity userOnline)
    {
        OnlineSessionEntity onlineSession = userOnline.getSession();
        if (StringUtils.isNotNull(onlineSession) && onlineSession.getId() == null)
        {
            onlineSession.setId(userOnline.getSessionId());
        }
        return userOnline.getSession();
    }

    @Override
    public Session createSession(SessionContext initData)
    {
        OnlineSessionEntity session = new OnlineSessionEntity();
        if (initData != null && initData instanceof WebSessionContext)
        {
            WebSessionContext sessionContext = (WebSessionContext) initData;
            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();
            if (request != null)
            {
                UserAgent userAgent = UserAgent
                        .parseUserAgentString(ServletUtils.getHttpServletRequest().getHeader("UserEntity-Agent"));
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                session.setHost(IpUtils.getIpAddr(request));
                session.setBrowser(browser);
                session.setOs(os);
            }
        }
        return session;
    }
}
