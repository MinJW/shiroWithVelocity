package com.JWmes.project.monitor.job.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.monitor.job.entity.JobEntity;
import com.JWmes.project.monitor.job.service.JobService;
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
 * 调度任务信息操作处理
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/monitor/job")
public class JobController extends BaseController
{
    private String prefix = "monitor/job";

    @Autowired
    private JobService jobService;

    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String job()
    {
        return prefix + "/job";
    }

    @RequiresPermissions("monitor:jobEntity:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(JobEntity jobEntity)
    {
        setPageInfo(jobEntity);
        List<JobEntity> list = jobService.selectJobList(jobEntity);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @Log(title = "监控管理", action = "定时任务-删除调度")
    @RequiresPermissions("monitor:job:remove")
    @RequestMapping("/remove/{jobId}")
    @ResponseBody
    public MessageEntity remove(@PathVariable("jobId") Long jobId)
    {
        JobEntity jobEntity = jobService.selectJobById(jobId);
        if (jobEntity == null)
        {
            return MessageEntity.error("调度任务不存在");
        }
        if (jobService.deleteJob(jobEntity) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @Log(title = "监控管理", action = "定时任务-批量删除")
    @RequiresPermissions("monitor:job:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        try
        {
            jobService.batchDeleteJob(ids);
            return MessageEntity.ok();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return MessageEntity.error(e.getMessage());
        }
    }

    /**
     * 任务调度状态修改
     */
    @Log(title = "监控管理", action = "定时任务-状态修改")
    @RequiresPermissions("monitor:jobEntity:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public MessageEntity changeStatus(JobEntity jobEntity)
    {
        if (jobService.changeStatus(jobEntity) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 新增调度
     */
    @Log(title = "监控管理", action = "定时任务-新增调度")
    @RequiresPermissions("monitor:job:add")
    @GetMapping("/add")
    public String add(Model model)
    {
        return prefix + "/add";
    }

    /**
     * 修改调度
     */
    @Log(title = "监控管理", action = "定时任务-修改调度")
    @RequiresPermissions("monitor:job:edit")
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, Model model)
    {
        JobEntity jobEntity = jobService.selectJobById(jobId);
        model.addAttribute("jobEntity", jobEntity);
        return prefix + "/edit";
    }

    /**
     * 保存调度
     */
    @Log(title = "监控管理", action = "定时任务-保存调度")
    @RequiresPermissions("monitor:jobEntity:save")
    @PostMapping("/save")
    @ResponseBody
    public MessageEntity save(JobEntity jobEntity)
    {
        if (jobService.saveJobCron(jobEntity) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }
}
