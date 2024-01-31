package com.gdutelc.service;

import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.utils.OkHttpUtils;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Map;

import static com.gdutelc.common.constant.RoleConstant.GRADUATE;
import static com.gdutelc.common.constant.RoleConstant.UNDER_GRADUATE;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/1/31 23:55
 * ExamScoreServiceImpl
 */
public interface ExamScoreService {


    /**
     * 查询成绩
     *
     * @param baseRequestDto
     * @return
     */
    public Map<String, ArrayList<ExamScoreDto>> getExamScore(BaseRequestDto baseRequestDto);

}
