package com.gdutelc.controller.wechat;

import com.gdutelc.domain.DTO.BaseRequestDto;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.DTO.VerCodeDto;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.service.impl.LoginServiceImpl;
import com.gdutelc.service.impl.NotificationServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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

    @ApiModelProperty(value = "获取登录验证码")
    @GetMapping("/sendVer")
    public AjaxResult sendVerification(HttpServletRequest request) {
        String jSessionId = request.getParameter("jSessionId");
        VerCodeDto verCodeDto = gdutDayService.sendVerification(jSessionId);
        return AjaxResult.success(verCodeDto);
    }

    @ApiModelProperty(value = "获得图书馆二维码")
    @GetMapping("/libQr")
    public AjaxResult libQr(@RequestParam(name = "stuId") String stuId,
                            @RequestParam(name = "width") Integer width,
                            @RequestParam(name = "height") Integer height) {
        LibQrVO libQrVO = new LibQrVO(stuId, width, height);
        String qr = gdutDayService.getLibQr(libQrVO);
        return AjaxResult.success(qr);
    }


    @ApiModelProperty(value = "获得课表信息")
    @GetMapping("/schedule")
    public AjaxResult schedule(@RequestParam(name = "cookies") String cookies,
                               @RequestParam(name = "userType") Integer userType,
                               @RequestParam(name = "termId") Integer termId) {
        return AjaxResult.success(gdutDayService.getScheduleInfo(cookies, userType, termId));
    }

    @ApiModelProperty(value = "获得成绩")
    @GetMapping("/score")
    public AjaxResult exam(@RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(gdutDayService.getExamScore(baseRequestDto.getCookies(), baseRequestDto.getUserType()));
    }

    @ApiModelProperty(value = "获得用户信息")
    @PostMapping("/userInfo")
    public AjaxResult userInfo(@RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(gdutDayService.getUserInfo(baseRequestDto.getCookies(), baseRequestDto.getUserType()));
    }

    @ApiModelProperty(value = "获得考试安排")
    @GetMapping("/examination")
    public AjaxResult examination(@RequestParam(name = "cookies") String cookies,
                                  @RequestParam(name = "userType") Integer userType) {
        return AjaxResult.success(gdutDayService.getExaminationInfo(cookies, userType));
    }


}

