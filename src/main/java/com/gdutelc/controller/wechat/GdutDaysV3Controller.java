package com.gdutelc.controller.wechat;

import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.query.ScheduleInfoQueryDto;
import com.gdutelc.domain.DTO.VerCodeDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.service.impl.LoginServiceImpl;
import com.gdutelc.service.impl.NotificationServiceImpl;
import com.gdutelc.service.impl.ScheduleInfoServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @Resource
    private ScheduleInfoServiceImpl scheduleInfoService;

    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody GdutDayWechatUser gdutDayWechatUser) {
        // V3解密
        gdutDayWechatUser.setPassword(loginService.loginDecrypt(gdutDayWechatUser.getPassword()));
        LoginDto loginDto = loginService.gdutDayWechatUserLogin(gdutDayWechatUser);
        return AjaxResult.success(loginDto);
    }

    /**
     * 检查是否需要滑块
     * @param user user
     * @return bool
     */
    @GetMapping("/check/{user}")
    public AjaxResult check(@PathVariable(value="user") String user){
        return AjaxResult.success(loginService.checkBlock(user));
    }


    @GetMapping("/sendVer")
    public AjaxResult sendVerification(HttpServletRequest request) {
        String jSessionId = request.getParameter("jSessionId");
        VerCodeDto verCodeDto = gdutDayService.sendVerification(jSessionId);
        return AjaxResult.success(verCodeDto);
    }

    @GetMapping("/libQr")
    public AjaxResult libQr(@RequestParam(name = "stuId") String stuId,
                            @RequestParam(name = "width") Integer width,
                            @RequestParam(name = "height") Integer height) {
        LibQrVO libQrVO = new LibQrVO(stuId, width, height);
        String qr = gdutDayService.getLibQr(libQrVO);
        return AjaxResult.success(qr);
    }


    @PostMapping("/schedule")
    public AjaxResult schedule(@Validated @RequestBody ScheduleInfoQueryDto queryDto) {
        return AjaxResult.success(scheduleInfoService.getScheduleInfo(queryDto));
    }

    @GetMapping("/score")
    public AjaxResult exam(@RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(gdutDayService.getExamScore(baseRequestDto.getCookies(), baseRequestDto.getUserType()));
    }

    @PostMapping("/userInfo")
    public AjaxResult userInfo(@RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(gdutDayService.getUserInfo(baseRequestDto.getCookies(), baseRequestDto.getUserType()));
    }

    @GetMapping("/examination")
    public AjaxResult examination(@RequestParam(name = "cookies") String cookies,
                                  @RequestParam(name = "userType") Integer userType) {
        return AjaxResult.success(gdutDayService.getExaminationInfo(cookies, userType));
    }


}

