package com.gdutelc.service;

import com.gdutelc.domain.DTO.CetScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;

import java.util.List;

/**
 * @author xb2555
 * @version 1.0
 * @since 2026/4/15
 * CetScoreService 英语考级成绩服务
 */
public interface CetScoreService {

    /**
     * 查询英语考级成绩
     *
     * @param baseRequestDto query
     * @return list
     */
    List<CetScoreDto> getCetScore(BaseRequestDto baseRequestDto);
}
