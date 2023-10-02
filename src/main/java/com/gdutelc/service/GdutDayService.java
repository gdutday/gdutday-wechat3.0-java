package com.gdutelc.service;

import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.DTO.ExaminationDto;
import com.gdutelc.domain.DTO.ScheduleInfoDto;
import com.gdutelc.domain.DTO.UserInfoDto;
import com.gdutelc.domain.VO.BasicVO;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;

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
    public UserInfoDto getUserInfo(String  cookies);


    /**
     * 获得课程信息
     *
     * @param cookies cookies
     * @return ArrayList<ScheduleInfoDto>
     */
    public ArrayList<ScheduleInfoDto> getScheduleInfo(String cookies);


    /**
     * 获得考试信息
     *
     * @param cookies cookies
     * @return ArrayList
     */
    public ArrayList<ExamScoreDto> getExamScore(String cookies);

    /**
     * 获得图书馆二维码
     *
     * @param libQrVO 学号，宽度，高度
     * @return 返回图片的Base64编码
     */
    public String getLibQr(LibQrVO libQrVO);

    /**
     * 获得学生考试安排，仅本科生使用,研究生课堂公布
     *
     * @param cookies cookies
     * @return ArrayList
     */
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies);
}
