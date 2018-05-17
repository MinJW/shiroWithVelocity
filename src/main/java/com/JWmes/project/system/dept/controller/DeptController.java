package com.JWmes.project.system.dept.controller;

import java.util.List;
import java.util.Map;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.project.system.dept.entity.DeptEntity;
import com.JWmes.project.system.dept.service.DeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 部门信息
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController
{
    private String prefix = "system/dept";

    @Autowired
    private DeptService deptService;

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept()
    {
        return prefix + "/dept";
    }

    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    @ResponseBody
    public List<DeptEntity> list()
    {
        List<DeptEntity> deptList = deptService.selectDeptAll();
        return deptList;
    }

    /**
     * 修改
     */
    @Log(title = "系统管理", action = "部门管理-修改部门")
    @RequiresPermissions("system:dept:edit")
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, Model model)
    {
        DeptEntity dept = deptService.selectDeptById(deptId);
        model.addAttribute("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 新增
     */
    @Log(title = "系统管理", action = "部门管理-新增部门")
    @RequiresPermissions("system:dept:add")
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, Model model)
    {
        DeptEntity dept = deptService.selectDeptById(parentId);
        model.addAttribute("dept", dept);
        return prefix + "/add";
    }

    /**
     * 保存
     */
    @Log(title = "系统管理", action = "部门管理-保存部门")
    @RequiresPermissions("system:dept:save")
    @PostMapping("/save")
    @ResponseBody
    public MessageEntity save(DeptEntity dept)
    {
        if (deptService.saveDept(dept) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 删除
     */
    @Log(title = "系统管理", action = "部门管理-删除部门")
    @RequiresPermissions("system:dept:remove")
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public MessageEntity remove(@PathVariable("deptId") Long deptId)
    {
        if (deptService.selectDeptCount(deptId) > 0)
        {
            return MessageEntity.error( "存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return MessageEntity.error("部门存在用户,不允许删除");
        }
        if (deptService.deleteDeptById(deptId) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public MessageEntity checkDeptNameUnique(DeptEntity dept)
    {
        String uniqueFlag = "0";
        if (dept != null)
        {
            uniqueFlag = deptService.checkDeptNameUnique(dept);
        }
        return MessageEntity.okWithResult(uniqueFlag);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, Model model)
    {
        model.addAttribute("treeName", deptService.selectDeptById(deptId).getDeptName());
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public MessageEntity treeData()
    {
        List<Map<String, Object>> tree = deptService.selectDeptTree();
        return MessageEntity.okWithResult(tree);
    }
}
