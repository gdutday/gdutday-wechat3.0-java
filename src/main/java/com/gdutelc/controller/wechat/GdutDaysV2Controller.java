package com.gdutelc.controller.wechat;

import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:50
 * GdutDaysV2Controller for compatibility with v2
 */
@RestController
@RequestMapping("/gdutDay2/stu")
public class GdutDaysV2Controller {

    @Autowired
    private NotificationServiceImpl notificationService;
    /**
     * 服务显示
     * @return 服务显示
     */
    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

    /**
     * 获得学生课表
     * @param jSessionId session
     * @return 学生课表
     */
    @GetMapping(value = "/getAllClazzes.elc")
    @ResponseBody
    public String getAllClazzes(String jSessionId) {
        return "{\"code\":-1,\"msg\":\"请使用最新版本的小程序\"}";
    }

    /**
     * 获取学生成绩
     *
     * @return 学生成绩
     */
    @GetMapping(value = "/getStuGrades.elc")
    @ResponseBody
    public String getStuGrades(String jSessionId) {
        return "{\"code\":-1,\"msg\":\"请使用最新版本的小程序\"}";
    }

    /**
     * 获取考试安排
     *
     * @return 考试安排
     */
    @GetMapping(value = "/getStuExaminations.elc")
    @ResponseBody
    public String getStuExaminations(String jSessionId) {
        return "{\"code\":-1,\"msg\":\"请使用最新版本的小程序\"}";
    }

    /**
     * 获得四、六级成绩
     *
     * @param jSessionId session
     * @return 四、六级成绩
     */
    @GetMapping(value = "/getStuCET.elc")
    @ResponseBody
    public String getStuCET(String jSessionId) {
        return "{\"code\":-1,\"msg\":\"请使用最新版本的小程序\"}";
    }

    /**
     * 获得图书馆QR
     *
     * @param stuId
     * @param widthStr
     * @param heightStr
     * @return
     */
    @GetMapping(value = "/getLibQrByStuId.elc")
    @ResponseBody
    public String getLibQrByStuId(String stuId, String widthStr, String heightStr) {
        return "{\"code\":-1,\"msg\":\"请使用最新版本的小程序\"}";
    }
}
