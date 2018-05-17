package com.JWmes.project.system.user.controller;

import java.util.List;

import com.JWmes.framework.config.JWmesConfig;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.project.system.menu.entity.MenuEntity;
import com.JWmes.project.system.menu.service.MenuService;
import com.JWmes.project.system.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页 业务处理
 * 
 * @author zhong
 */
@Controller
public class IndexController extends BaseController
{
    @Autowired
    private MenuService menuService;

    @Autowired
    private JWmesConfig jWmesConfig;

    // 系统首页
    @GetMapping("/index")
    public String index(Model model)
    {
        // 取身份信息
        UserEntity user = getUser();
        // 根据用户id取出菜单
        List<MenuEntity> menus = menuService.selectMenusByUserId(user.getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("user", user);
        model.addAttribute("copyrightYear", jWmesConfig.getCopyrightYear());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(Model model)
    {
        model.addAttribute("version", jWmesConfig.getVersion());
        return "main";
    }

}
