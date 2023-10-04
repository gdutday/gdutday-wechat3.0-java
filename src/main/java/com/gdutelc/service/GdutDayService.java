package com.gdutelc.service;

import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;

import java.util.ArrayList;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:11
 * UserInfoService
 */
public interface GdutDayService {

    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return UserInfoDto
     */
     UserInfoDto getUserInfo(String  cookies,Integer userType);


    /**
     * 获得课程信息
     *
     * @param cookies cookies
     * @return ArrayList<ScheduleInfoDto>
     */
     ArrayList<ScheduleInfoDto> getScheduleInfo(String cookies,Integer userType);


    /**
     * 获得考试信息
     *
     * @param cookies cookies
     * @return ArrayList
     */
     ArrayList<ExamScoreDto> getExamScore(String cookies,Integer userType);

    /**
     * 获得图书馆二维码
     *
     * @param libQrVO 学号，宽度，高度
     * @return 返回图片的Base64编码
     */
     String getLibQr(LibQrVO libQrVO);

    /**
     * 获得学生考试安排，仅本科生使用,研究生课堂公布
     *
     * @param cookies cookies
     * @return ArrayList
     */
     ArrayList<ExaminationDto> getExaminationInfo(String cookies,Integer userType);

    /**
     * 获取登录验证码
     * @param jSessionId
     * @return
     */
    VerCodeDto sendVerification(String jSessionId);
}
