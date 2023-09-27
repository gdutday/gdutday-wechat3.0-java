package com.gdutelc.controller.wechat;

import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:50
 * GdutDaysV2Controller for compatibility with v2
 */
@RestController
@RequestMapping("/gdutDay2")
public class GdutDaysV2Controller {

    @Autowired
    private NotificationServiceImpl notificationService;
    /**
     * 服务显示
     * @return
     */
    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

}
