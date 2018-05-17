package com.JWmes.project.monitor.logininfor.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.monitor.logininfor.entity.LogininforEntity;
import com.JWmes.project.monitor.logininfor.service.LogininforService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统访问记录
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class LogininforController extends BaseController
{
    private String prefix = "monitor/logininfor";

    @Autowired
    private LogininforService logininforService;

    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String LogininforEntity()
    {
        return prefix + "/logininfor";
    }

    @RequiresPermissions("monitor:logininfor:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogininforEntity LogininforEntity)
    {
        setPageInfo(LogininforEntity);
        List<LogininforEntity> list = logininforService.selectLogininforEntityList(LogininforEntity);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:logininfor:batchRemove")
    @Log(title = "监控管理", action = "登录日志-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = logininforService.batchDeleteLogininforEntity(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }
}
