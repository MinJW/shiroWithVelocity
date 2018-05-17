package com.JWmes.project.system.role.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.system.role.entity.RoleEntity;
import com.JWmes.project.system.role.service.RoleService;
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

/**
 * 角色信息
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController
{

    private String prefix = "system/role";

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role()
    {
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(RoleEntity role)
    {
        setPageInfo(role);
        List<RoleEntity> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 新增角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "系统管理", action = "角色管理-新增角色")
    @GetMapping("/add")
    public String add(Model model)
    {
        return prefix + "/add";
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "系统管理", action = "角色管理-修改角色")
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, Model model)
    {
        RoleEntity role = roleService.selectRoleById(roleId);
        model.addAttribute("role", role);
        return prefix + "/edit";
    }

    /**
     * 保存角色
     */
    @RequiresPermissions("system:role:save")
    @Log(title = "系统管理", action = "角色管理-保存角色")
    @PostMapping("/save")
    @Transactional(rollbackFor=Exception.class)
    @ResponseBody
    public MessageEntity save(RoleEntity role)
    {
        if (roleService.saveRole(role) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "系统管理", action = "角色管理-删除角色")
    @RequestMapping("/remove/{roleId}")
    @Transactional(rollbackFor=Exception.class)
    @ResponseBody
    public MessageEntity remove(@PathVariable("roleId") Long roleId)
    {
        RoleEntity role = roleService.selectRoleById(roleId);
        if (role == null)
        {
            return MessageEntity.error("角色不存在");
        }
        if (roleService.selectCountUserRoleByRoleId(roleId) > 0)
        {
            return MessageEntity.error("角色已分配,不能删除");
        }
        if (roleService.deleteRoleById(roleId) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("system:role:batchRemove")
    @Log(title = "系统管理", action = "角色管理-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        for(Long roleId:ids)
        if (roleService.selectCountUserRoleByRoleId(roleId) > 0)
        {
            return MessageEntity.error("有角色已分配,不能批量删除");
        }

        int rows = roleService.batchDeleteRole(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public MessageEntity checkRoleNameUnique(RoleEntity role)
    {
        String uniqueFlag = "0";
        if (role != null)
        {
            uniqueFlag = roleService.checkRoleNameUnique(role);
        }
        return MessageEntity.okWithResult(uniqueFlag);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree()
    {
        return prefix + "/tree";
    }

}