package com.gdutelc.controller.wechat;

import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.service.impl.LoginServiceImpl;
import com.gdutelc.service.impl.NotificationServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
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

    @Resource
    private LoginServiceImpl loginService;

    @Resource
    private GdutDayService gdutDayService;

    @ApiModelProperty(value = "测试接口")
    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody GdutDayWechatUser gdutDayWechatUser) {
        // 如果是V2 走一遍VO转换
        LoginDto loginDto = loginService.gdutDayWechatUserLogin(gdutDayWechatUser);
        return AjaxResult.success(loginDto);
    }

    @ApiModelProperty(value = "获得图书馆二维码")
    @GetMapping("/libQr")
    public AjaxResult libQr(@RequestParam("stuId") String stuId,
                            @RequestParam("width") Integer width,
                            @RequestParam("height") Integer height) {
        LibQrVO libQrVO = new LibQrVO(stuId, width, height);
        String qr = gdutDayService.getLibQr(libQrVO);
        return AjaxResult.success(qr);
    }


    @ApiModelProperty(value = "获得课表信息")
    @GetMapping("/schedule")
    public AjaxResult schedule(String cookies) {
        return AjaxResult.success(gdutDayService.getScheduleInfo(cookies));
    }

    @ApiModelProperty(value = "获得成绩")
    @GetMapping("/score")
    public AjaxResult exam(String cookies) {
        return AjaxResult.success(gdutDayService.getExamScore(cookies));
    }

    @ApiModelProperty(value = "获得用户信息")
    @GetMapping("/userInfo")
    public AjaxResult userInfo(String cookies) {
        return AjaxResult.success(gdutDayService.getUserInfo(cookies));
    }

    @ApiModelProperty(value = "获得考试安排")
    @GetMapping("/examination")
    public AjaxResult examination(String cookies) {
        return AjaxResult.success(gdutDayService.getExaminationInfo(cookies));
    }


}

