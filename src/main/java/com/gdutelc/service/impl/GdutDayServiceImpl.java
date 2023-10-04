package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.utils.GdutDayCookieJar;
import com.gdutelc.utils.LiUtils;
import com.gdutelc.utils.OkHttpUtils;
import com.gdutelc.utils.StringUtils;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:32
 * GdutDaServiceImpl
 */
@Service
public class GdutDayServiceImpl implements GdutDayService {

    @Resource
    private OkHttpUtils okHttpUtils;
    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return 用户dto
     */
    @Override
    public UserInfoDto getUserInfo(String cookies,Integer userType) {
        return null;
    }

    /**
     * 获得课表信息
     *
     * @param cookies cookies
     * @return 课表 Dto
     */
    @Override
    public ArrayList<ScheduleInfoDto> getScheduleInfo(String cookies,Integer userType) {
        return null;
    }

    /**
     * 获得考试信息
     *
     * @param cookies cookies
     */
    @Override
    public ArrayList<ExamScoreDto> getExamScore(String cookies,Integer userType) {
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
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies,Integer userType) {
        return null;
    }

    @Override
    public VerCodeDto sendVerification(String jSessionId) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        Response response = null;
        if(StringUtils.isEmpty(jSessionId)){
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis());
        }else {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis(),jSessionId);
        }
        if(response.code()!=200){
            throw new ServiceException("获取验证码失败", response.code());
        }
        String header = response.header("Set-Cookie");
        if(StringUtils.isEmpty(header)){
            header = jSessionId;
        }
        String base64 = null;
        try {
            byte[] bytes = response.body().bytes();
            base64 = LiUtils.makeBase64(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new VerCodeDto(base64,header);
    }
}
