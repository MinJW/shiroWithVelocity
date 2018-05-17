package com.JWmes.framework.web.exception;

import com.JWmes.common.exception.DemoModeException;
import com.JWmes.framework.web.entity.MessageEntity;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 自定义异常处理器
 * 
 * @author zhong
 */
@RestControllerAdvice
public class DefaultExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    
    /**
     * 权限校验失败
     */
    @ExceptionHandler(AuthorizationException.class)
    public MessageEntity handleAuthorizationException(AuthorizationException e)
    {
        log.error(e.getMessage(), e);
        return MessageEntity.error("您没有数据的权限，请联系管理员添加");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public MessageEntity handleException(HttpRequestMethodNotSupportedException e)
    {
        log.error(e.getMessage(), e);
        return MessageEntity.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public MessageEntity notFount(RuntimeException e)
    {
        log.error("运行时异常:", e);
        return MessageEntity.error("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public MessageEntity handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return MessageEntity.error("服务器错误，请联系管理员");
    }
    
    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public MessageEntity demoModeException(DemoModeException e)
    {
        return MessageEntity.error("演示模式，不允许操作");
    }

}
