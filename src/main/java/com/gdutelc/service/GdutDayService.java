package com.gdutelc.service;

import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.domain.query.ScheduleInfoQueryDto;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Map;

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
     * @param queryDto QueryDaya
     * @return ArrayList<ScheduleInfoDto>
     */
     Map<String,ArrayList<ScheduleInfoDto>> getScheduleInfo(ScheduleInfoQueryDto queryDto);


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
     * @param term
     * @return ArrayList
     */
     ArrayList<ExaminationDto> getExaminationInfo(String cookies, Integer userType, Integer term);

    /**
     * 获取登录验证码
     * @param jSessionId jSessionId
     * @return base64编码的验证码
     */
    VerCodeDto sendVerification(String jSessionId);

    /**
     * 获取学期
     * @param baseRequestDto
     * @return
     */
    String getTerm(BaseRequestDto baseRequestDto);
}
