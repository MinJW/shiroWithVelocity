package com.JWmes.framework.web.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作消息提醒
 * 
 * @author zhong
 */
public class MessageEntity extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /**
     * 初始化一个新创建的 MessageEntity 对象，默认成功。
     */
    public MessageEntity()
    {
        put("code", 200);
        put("msg", "操作成功");
        put("data", null);
    }

    /**
     * 返回错误消息
     * 
     * @return 错误消息
     */
    public static MessageEntity error()
    {
        return error(100, "操作失败",null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 内容
     * @return 错误消息
     */
    public static MessageEntity error(String msg)
    {
        return error(100, msg,null);
    }
    /**
     * 返回错误消息
     *
     * @param code 错误码
     * @param msg 内容
     * @return 错误消息
     */
    public static MessageEntity error(int code, String msg)
    {
        MessageEntity json = new MessageEntity();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", null);
        return json;
    }
    /**
     * 返回错误消息
     * 
     * @param code 错误码
     * @param msg 内容
     * @return 错误消息
     */
    public static MessageEntity error(int code, String msg,Object  data)
    {
        MessageEntity json = new MessageEntity();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json;
    }

    /**
     * 返回成功消息
     * 
     * @param msg 内容
     * @return 成功消息
     */
    public static MessageEntity ok(String msg)
    {
        MessageEntity json = new MessageEntity();
        json.put("msg", msg);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static MessageEntity okWithResult(Object o)
    {
        MessageEntity json = new MessageEntity();
        json.put("data",o);
        return json;
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static MessageEntity ok()
    {
        return new MessageEntity();
    }

    /**
     * 返回成功消息
     * 
     * @param key 键值
     * @param value 内容
     * @return 成功消息
     */
    @Override
    public MessageEntity put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
