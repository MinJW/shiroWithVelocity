package com.JWmes.project.system.user.controller;

import java.util.List;

import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.system.post.entity.PostEntity;
import com.JWmes.project.system.post.service.PostService;
import com.JWmes.project.system.role.entity.RoleEntity;
import com.JWmes.project.system.role.service.RoleService;
import com.JWmes.project.system.user.entity.UserEntity;
import com.JWmes.project.system.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;


/**
 * 用户信息
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController
{

    private String prefix = "system/user";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PostService postService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserEntity user)
    {
        user.setDelFlag(0);
        setPageInfo(user);
        List<UserEntity> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 修改用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "系统管理", action = "用户管理-修改用户")
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, Model model)
    {
        UserEntity user = userService.selectUserById(userId);
        List<RoleEntity> roles = roleService.selectRolesByUserId(userId);
        List<PostEntity> posts = postService.selectPostsByUserId(userId);
        model.addAttribute("roles", roles);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return prefix + "/edit";
    }

    /**
     * 新增用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "系统管理", action = "用户管理-新增用户")
    @GetMapping("/add")
    public String add(Model model)
    {
        List<RoleEntity> roles = roleService.selectRoleAll();
        List<PostEntity> posts = postService.selectPostAll();
        model.addAttribute("roles", roles);
        model.addAttribute("posts", posts);
        return prefix + "/add";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "系统管理", action = "用户管理-重置密码")
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, Model model)
    {
        UserEntity user = userService.selectUserById(userId);
        model.addAttribute("user", user);
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "系统管理", action = "用户管理-重置密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    public MessageEntity resetPwd(UserEntity user)
    {
        int rows = userService.resetUserPwd(user);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "系统管理", action = "用户管理-删除用户")
    @RequestMapping("/remove/{userId}")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public MessageEntity remove(@PathVariable("userId") Long userId)
    {
        UserEntity user = userService.selectUserById(userId);
        if (user == null)
        {
            return MessageEntity.error("用户不存在");
        }
        if (userService.deleteUserById(userId) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("system:user:batchRemove")
    @Log(title = "系统管理", action = "用户管理-批量删除")
    @PostMapping("/batchRemove")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = userService.batchDeleteUser(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 保存用户
     */
    @RequiresPermissions("system:user:save")
    @Log(title = "系统管理", action = "用户管理-保存用户")
    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public MessageEntity save(UserEntity user)
    {
        //补充数据
        UserEntity loginUser=getUser();
        user.setDelFlag(0);
        user.setCreateBy(loginUser.getLoginName());
        if (userService.saveUser(user) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkUserNameUnique")
    @ResponseBody
    public MessageEntity checkUserNameUnique(UserEntity user)
    {
        String uniqueFlag = "0";
        if (user != null)
        {
            uniqueFlag = userService.checkUserNameUnique(user.getLoginName());
        }
        return MessageEntity.okWithResult(uniqueFlag);
    }

}