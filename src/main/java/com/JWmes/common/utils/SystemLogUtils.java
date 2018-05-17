package com.JWmes.common.utils;


import com.JWmes.common.constant.CommonConstant;
import com.JWmes.common.utils.security.ShiroUtils;
import com.JWmes.common.utils.spring.SpringUtils;
import com.JWmes.project.monitor.logininfor.entity.LogininforEntity;
import com.JWmes.project.monitor.logininfor.service.impl.LogininforServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记录用户日志信息
 * 
 * @author zhong
 */
public class SystemLogUtils
{

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录格式 [ip][用户名][操作][错误消息]
     * <p/>
     * 注意操作如下： loginError 登录失败 loginSuccess 登录成功 passwordError 密码错误 changePassword 修改密码 changeStatus 修改状态
     *
     * @param username
     * @param
     * @param msg
     * @param args
     */
    public static void log(String username, String status, String msg, Object... args)
    {
        StringBuilder s = new StringBuilder();
        s.append(LogUtils.getBlock(ShiroUtils.getIp()));
        s.append(LogUtils.getBlock(username));
        s.append(LogUtils.getBlock(status));
        s.append(LogUtils.getBlock(msg));

        sys_user_logger.info(s.toString(), args);

        if (CommonConstant.LOGIN_SUCCESS.equals(status) || CommonConstant.LOGOUT.equals(status))
        {
            saveOpLog(username, msg, CommonConstant.SUCCESS);
        }
        else if (CommonConstant.LOGIN_FAIL.equals(status))
        {
            saveOpLog(username, msg, CommonConstant.FAIL);
        }
    }

    public static void saveOpLog(String username, String message, String status)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getHttpServletRequest().getHeader("UserEntity-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        LogininforServiceImpl LogininforEntityService = SpringUtils.getBean(LogininforServiceImpl.class);
        LogininforEntity LogininforEntity = new LogininforEntity();
        LogininforEntity.setLoginName(username);
        LogininforEntity.setStatus(status);
        LogininforEntity.setIpaddr(ShiroUtils.getIp());
        LogininforEntity.setBrowser(browser);
        LogininforEntity.setOs(os);
        LogininforEntity.setMsg(message);
        LogininforEntityService.insertLogininforEntity(LogininforEntity);
    }
}
