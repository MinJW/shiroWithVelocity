package com.JWmes.common.exception.base;

/**
 * Dao异常
 * 
 * @author zhong
 */
public class MapperException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public MapperException(String defaultMessage)
    {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }

    @Override
    public String toString()
    {
        return this.getClass() + "{" + "message='" + getMessage() + '\'' + '}';
    }
}
