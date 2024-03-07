package com.gdutelc.service;

import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;

import java.util.ArrayList;
import java.util.Map;

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
     * @param baseRequestDto query
     * @return
     */
    public Map<String, ArrayList<ExamScoreDto>> getExamScore(BaseRequestDto baseRequestDto);

}
