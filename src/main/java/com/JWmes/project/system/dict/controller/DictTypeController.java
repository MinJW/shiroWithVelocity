package com.JWmes.project.system.dict.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.system.dict.entity.DictTypeEntity;
import com.JWmes.project.system.dict.service.DictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 数据字典信息
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/dict")
public class DictTypeController extends BaseController
{
    private String prefix = "system/dict/type";

    @Autowired
    private DictTypeService dictTypeService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictType()
    {
        return prefix + "/type";
    }

    @GetMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(DictTypeEntity dictType)
    {
        setPageInfo(dictType);
        List<DictTypeEntity> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    /**
     * 修改字典类型
     */
    @Log(title = "系统管理", action = "字典管理-修改字典类型")
    @RequiresPermissions("system:dict:edit")
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") Long dictId, Model model)
    {
        DictTypeEntity dict = dictTypeService.selectDictTypeById(dictId);
        model.addAttribute("dict", dict);
        return prefix + "/edit";
    }

    /**
     * 新增字典类型
     */
    @Log(title = "系统管理", action = "字典管理-新增字典类型")
    @RequiresPermissions("system:dict:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 保存字典类型
     */
    @Log(title = "系统管理", action = "字典管理-保存字典类型")
    @RequiresPermissions("system:dict:save")
    @PostMapping("/save")
    @ResponseBody
    public MessageEntity save(DictTypeEntity dict)
    {
        if (dictTypeService.saveDictType(dict) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }
    
    /**
     * 删除
     */
    @Log(title = "系统管理", action = "字典管理-删除字典类型")
    @RequiresPermissions("system:dict:remove")
    @RequestMapping("/remove/{dictId}")
    @ResponseBody
    public MessageEntity remove(@PathVariable("dictId") Long dictId)
    {
        DictTypeEntity dictType = dictTypeService.selectDictTypeById(dictId);
        if (dictType == null)
        {
            return MessageEntity.error("字典不存在");
        }
        if (dictTypeService.deleteDictTypeById(dictId) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @Log(title = "系统管理", action = "字典类型-批量删除")
    @RequiresPermissions("system:dict:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = dictTypeService.batchDeleteDictType(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 查询字典详细
     */
    @Log(title = "系统管理", action = "字典管理-查询字典数据")
    @RequiresPermissions("system:dict:list")
    @GetMapping("/detail/{dictId}")
    public String detail(@PathVariable("dictId") Long dictId, Model model)
    {
        DictTypeEntity dict = dictTypeService.selectDictTypeById(dictId);
        model.addAttribute("dict", dict);
        return "system/dict/data/data";
    }
    
    /**
     * 校验字典类型
     */
    @PostMapping("/checkDictTypeUnique")
    @ResponseBody
    public String checkDictTypeUnique(DictTypeEntity dictType)
    {
        String uniqueFlag = "0";
        if (dictType != null)
        {
            uniqueFlag = dictTypeService.checkDictTypeUnique(dictType);
        }
        return uniqueFlag;
    }
}
