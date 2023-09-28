package com.gdutelc.controller.wechat;

import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.framework.domain.VO.GdutDayWechatUser;
import com.gdutelc.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:52
 * GdutDaysV3Controller
 */
@RestController
@RequestMapping("/gdutDay3")
public class GdutDaysV3Controller {
    @Autowired
    private NotificationServiceImpl notificationService;

    /**
     * 服务显示
     *
     * @return 显示是否运行状态
     */
    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

    @PostMapping("/¬login")
    public AjaxResult login(@RequestBody GdutDayWechatUser gdutDayWechatUser) {

        return AjaxResult.success();
    }
}

