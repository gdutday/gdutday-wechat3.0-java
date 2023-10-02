package com.gdutelc.service.impl;

import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.DTO.ExaminationDto;
import com.gdutelc.domain.DTO.ScheduleInfoDto;
import com.gdutelc.domain.DTO.UserInfoDto;
import com.gdutelc.domain.VO.BasicVO;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.service.GdutDayService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:32
 * GdutDaServiceImpl
 */
@Service
public class GdutDaServiceImpl implements GdutDayService {
    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return 用户dto
     */
    @Override
    public UserInfoDto getUserInfo(String cookies) {
        return null;
    }

    /**
     * 获得课表信息
     *
     * @param cookies cookies
     * @return 课表 Dto
     */
    @Override
    public ArrayList<ScheduleInfoDto> getScheduleInfo(String cookies) {
        return null;
    }

    /**
     * 获得考试信息
     *
     * @param cookies cookies
     */
    @Override
    public ArrayList<ExamScoreDto> getExamScore(String cookies) {
        return null;
    }

    /**
     * 获得图书馆二维码
     *
     * @param libQrVO 学号，宽度，高度
     * @return 返回图片的Base64编码
     */
    @Override
    public String getLibQr(LibQrVO libQrVO) {
        return null;
    }

    /**
     * 获得考试安排
     *
     * @param cookies cookies
     * @return ArrayList
     */
    @Override
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies) {
        return null;
    }
}
