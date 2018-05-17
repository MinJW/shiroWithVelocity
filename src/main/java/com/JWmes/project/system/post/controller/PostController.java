package com.JWmes.project.system.post.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.system.post.entity.PostEntity;
import com.JWmes.project.system.post.service.PostService;
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
 * 岗位信息操作处理
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/system/post")
public class PostController extends BaseController
{
    private String prefix = "system/post";

    @Autowired
    private PostService postService;

    @RequiresPermissions("system:post:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/post";
    }

    @RequiresPermissions("system:post:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(PostEntity post)
    {
        setPageInfo(post);
        List<PostEntity> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @Log(title = "系统管理", action = "岗位管理-删除岗位")
    @RequiresPermissions("system:post:remove")
    @RequestMapping("/remove/{postId}")
    @ResponseBody
    public MessageEntity remove(@PathVariable("postId") Long postId)
    {
        PostEntity post = postService.selectPostById(postId);
        if (post == null)
        {
            return MessageEntity.error("岗位不存在");
        }
        if (postService.selectCountPostById(postId) > 0)
        {
            return MessageEntity.error("岗位已分配,不能删除");
        }
        if (postService.deletePostById(postId) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    @RequiresPermissions("system:post:batchRemove")
    @Log(title = "系统管理", action = "岗位管理-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public MessageEntity batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = postService.batchDeletePost(ids);
        if (rows > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

    /**
     * 新增岗位
     */
    @Log(title = "系统管理", action = "岗位管理-新增岗位")
    @RequiresPermissions("system:post:add")
    @GetMapping("/add")
    public String add(Model model)
    {
        return prefix + "/add";
    }

    /**
     * 修改岗位
     */
    @Log(title = "系统管理", action = "岗位管理-修改岗位")
    @RequiresPermissions("system:post:edit")
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, Model model)
    {
        PostEntity post = postService.selectPostById(postId);
        model.addAttribute("post", post);
        return prefix + "/edit";
    }

    /**
     * 保存岗位
     */
    @Log(title = "系统管理", action = "岗位管理-保存岗位")
    @RequiresPermissions("system:post:save")
    @PostMapping("/save")
    @ResponseBody
    public MessageEntity save(PostEntity post)
    {
        if (postService.savePost(post) > 0)
        {
            return MessageEntity.ok();
        }
        return MessageEntity.error();
    }

}
