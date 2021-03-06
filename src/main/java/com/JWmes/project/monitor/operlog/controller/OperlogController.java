package com.JWmes.project.monitor.operlog.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.monitor.operlog.entity.OperLogEntity;
import com.JWmes.project.monitor.operlog.service.OperLogService;
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
 * 操作日志记录
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/monitor/operlog")
public class OperlogController extends BaseController
{
    private String prefix = "monitor/operlog";

    @Autowired
    private OperLogService operLogService;

    @RequiresPermissions("monitor:operlog:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/operlog";
    }

    @RequiresPermissions("monitor:operlog:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(OperLogEntity operLog)
    {
        setPageInfo(operLog);
        List<OperLogEntity> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:operlog:batchRemove")
    @Log(title = "监控管理", action = "操作日志-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = operLogService.batchDeleteOperLog(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping("/detail/{operId}")
    public String detail(@PathVariable("operId") Long deptId, Model model)
    {
        OperLogEntity operLog = operLogService.selectOperLogById(deptId);
        model.addAttribute("operLog", operLog);
        return prefix + "/detail";
    }
}
