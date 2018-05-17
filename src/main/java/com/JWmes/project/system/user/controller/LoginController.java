package com.JWmes.project.system.user.controller;

import com.JWmes.framework.web.entity.MessageEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.JWmes.common.utils.StringUtils;
import com.JWmes.framework.web.controller.BaseController;

/**
 * 登录验证
 * 
 * @author zhong
 */
@Controller
public class LoginController extends BaseController
{

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public MessageEntity ajaxLogin(String username, String password)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            return MessageEntity.ok();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return MessageEntity.error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "/error/unauth";
    }
}
