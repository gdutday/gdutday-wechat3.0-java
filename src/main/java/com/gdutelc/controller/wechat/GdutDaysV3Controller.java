package com.gdutelc.controller.wechat;

import com.gdutelc.domain.query.AdmissionDateDto;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.query.ExaminationReqDto;
import com.gdutelc.domain.query.ScheduleInfoQueryDto;
import com.gdutelc.domain.DTO.VerCodeDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.service.ExamScoreService;
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

    @Resource
    private ExamScoreService examScoreService;

    /**
     * 测试
     * @return
     */
    @GetMapping("/test")
    public Object test() {
        return notificationService.getHiMessage();
    }

    /**
     * 登录
     * @param gdutDayWechatUser
     * @return
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody GdutDayWechatUser gdutDayWechatUser) {
//        // V3解密
//        gdutDayWechatUser.setPassword(loginService.loginDecrypt(gdutDayWechatUser.getPassword()));
        LoginDto loginDto = loginService.gdutDayWechatUserLogin(gdutDayWechatUser);
        return AjaxResult.success(loginDto);
    }

    /**
     * 检查是否需要滑块
     *
     * @param user user
     * @return bool
     */
    @GetMapping("/check/{user}")
    public AjaxResult check(@PathVariable(value = "user") String user) {
        return AjaxResult.success(loginService.checkBlock(user));
    }


    /**
     * 验证码
     * @param request
     * @return
     */
    @GetMapping("/sendVer")
    public AjaxResult sendVerification(HttpServletRequest request) {
        String jSessionId = request.getParameter("jSessionId");
        VerCodeDto verCodeDto = gdutDayService.sendVerification(jSessionId);
        return AjaxResult.success(verCodeDto);
    }

    /**
     * 图书馆二维码
     * @param stuId
     * @param width
     * @param height
     * @return
     */
    @GetMapping("/libQr")
    public AjaxResult libQr(@RequestParam(name = "stuId") String stuId,
                            @RequestParam(name = "width") Integer width,
                            @RequestParam(name = "height") Integer height) {
        LibQrVO libQrVO = new LibQrVO(stuId, width, height);
        String qr = gdutDayService.getLibQr(libQrVO);
        return AjaxResult.success(qr);
    }


    /**
     * 获取课表
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/schedule")
    public AjaxResult schedule(@Validated @RequestBody ScheduleInfoQueryDto queryDto) {
        return AjaxResult.success(scheduleInfoService.getScheduleInfo(queryDto));
    }

    /**
     * 获取成绩
     *
     * @param baseRequestDto
     * @return
     */
    @PostMapping("/score")
    public AjaxResult exam(@Validated @RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(examScoreService.getExamScore(baseRequestDto));
    }

    /**
     * 获得用户信息
     * @param baseRequestDto
     * @return
     */
    @PostMapping("/userInfo")
    public AjaxResult userInfo(@Validated @RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success(gdutDayService.getUserInfo(baseRequestDto.getCookies(), baseRequestDto.getUserType()));
    }

    /**
     * 获取考试安排
     *
     * @param reqDto
     * @return
     */
    @PostMapping("/examination")
    public AjaxResult examination(@Validated @RequestBody ExaminationReqDto reqDto) {
        return AjaxResult.success(gdutDayService.getExaminationInfo(reqDto.getCookies(), reqDto.getUserType(), reqDto.getTermId()));
    }

    /**
     * 获取学期
     *
     * @param baseRequestDto
     * @return
     */

    @PostMapping("/getTerm")
    public AjaxResult getTerm(@Validated @RequestBody BaseRequestDto baseRequestDto) {
        return AjaxResult.success("获取最新学期成功！", gdutDayService.getTerm(baseRequestDto));
    }

    @GetMapping("/admissionDate")
    public AjaxResult getTerm() {
        return AjaxResult.success("获取开学时间成功！", gdutDayService.getAdmissionDate());
    }

    /**
     * 修改开学时间，记得先解密
     *
     * @param admissionDateDto
     * @return
     */
    @PostMapping("/admissionDate")
    private AjaxResult changeTerm(@Validated @RequestBody AdmissionDateDto admissionDateDto) {
        String date = loginService.loginDecrypt(admissionDateDto.getAdmissionDate());
        return AjaxResult.success("修改开学时间为：" + date, gdutDayService.changeAdmissionDate(date));
    }
}

