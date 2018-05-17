package com.JWmes.project.monitor.online.controller;

import java.util.List;

import com.JWmes.framework.aspectj.lang.annotation.Log;
import com.JWmes.framework.shiro.session.OnlineSessionMapper;
import com.JWmes.framework.web.controller.BaseController;
import com.JWmes.framework.web.entity.MessageEntity;
import com.JWmes.framework.web.page.TableDataInfo;
import com.JWmes.project.monitor.online.entity.OnlineSessionEntity;
import com.JWmes.project.monitor.online.entity.UserOnlineEntity;
import com.JWmes.project.monitor.online.service.UserOnlineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 在线用户监控
 * 
 * @author zhong
 */
@Controller
@RequestMapping("/monitor/online")
public class UserOnlineController extends BaseController
{
    private String prefix = "monitor/online";

    @Autowired
    private UserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionMapper onlineSessionMapper;

    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online()
    {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserOnlineEntity userOnline)
    {
        setPageInfo(userOnline);
        List<UserOnlineEntity> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:online:batchForceLogout")
    @Log(title = "监控管理", action = "在线用户-批量强退用户")
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public MessageEntity batchForceLogout(@RequestParam("ids[]") String[] ids)
    {
        for (String sessionId : ids)
        {
            UserOnlineEntity online = userOnlineService.selectOnlineById(sessionId);
            if (online == null)
            {
                return MessageEntity.error("用户已下线");
            }
            OnlineSessionEntity onlineSession = (OnlineSessionEntity) onlineSessionMapper.readSession(online.getSessionId());
            if (onlineSession == null)
            {
                return MessageEntity.error("用户已下线");
            }
            onlineSession.setStatus(OnlineSessionEntity.OnlineStatus.off_line);
            online.setStatus(OnlineSessionEntity.OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
        }
        return MessageEntity.ok();
    }

    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "监控管理", action = "在线用户-强退用户")
    @RequestMapping("/forceLogout/{sessionId}")
    @ResponseBody
    public MessageEntity forceLogout(@PathVariable("sessionId") String sessionId)
    {
        UserOnlineEntity online = userOnlineService.selectOnlineById(sessionId);
        if (online == null)
        {
            return MessageEntity.error("用户已下线");
        }
        OnlineSessionEntity onlineSession = (OnlineSessionEntity) onlineSessionMapper.readSession(online.getSessionId());
        if (onlineSession == null)
        {
            return MessageEntity.error("用户已下线");
        }
        onlineSession.setStatus(OnlineSessionEntity.OnlineStatus.off_line);
        online.setStatus(OnlineSessionEntity.OnlineStatus.off_line);
        userOnlineService.saveOnline(online);
        return MessageEntity.ok();
    }

}
