package com.JWmes.project.system.user.controller;

import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.project.system.user.entity.UserEntity;
import com.JWmes.project.system.user.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;

/**
 * 个人信息 业务处理
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController
{
    private String prefix = "system/user/profile";

    @Autowired
    private UserService userService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(Model model)
    {
        UserEntity user = getUser();
        String sex = user.getSex();
        if ("0".equals(sex))
        {
            user.setSex("性别：男");
        }
        else if ("1".equals(sex))
        {
            user.setSex("性别：女");
        }
        String roleGroup = userService.selectUserRoleGroup(user.getUserId());
        String postGroup = userService.selectUserPostGroup(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("roleGroup", roleGroup);
        model.addAttribute("postGroup", postGroup);
        return prefix + "/profile";
    }

    @RequestMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        UserEntity user = getUser();
        String encrypt = new Md5Hash(user.getLoginName() + password + user.getSalt()).toHex().toString();
        if (user.getPassword().equals(encrypt))
        {
            return true;
        }
        return false;
    }

    @Log(title = "系统管理", action = "个人信息-重置密码")
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, Model model)
    {
        UserEntity user = userService.selectUserById(userId);
        model.addAttribute("user", user);
        return prefix + "/resetPwd";
    }

    @Log(title = "系统管理", action = "个人信息-重置密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    public MessageEntity resetPwd(UserEntity user)
    {
        int rows = userService.resetUserPwd(user);
        if (rows > 0)
        {
            setUser(userService.selectUserById(user.getUserId()));
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 修改用户
     */
    @Log(title = "系统管理", action = "个人信息-修改用户")
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, Model model)
    {
        UserEntity user = userService.selectUserById(userId);
        model.addAttribute("user", user);
        return prefix + "/edit";
    }

    /**
     * 修改用户
     */
    @Log(title = "系统管理", action = "个人信息-保存用户")
    @PostMapping("/update")
    @ResponseBody
    public MessageEntity update(UserEntity user)
    {
        if (userService.updateUser(user) > 0)
        {
            setUser(userService.selectUserById(user.getUserId()));
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }
}
