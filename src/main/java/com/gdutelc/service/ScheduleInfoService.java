package com.gdutelc.service;

import com.gdutelc.domain.DTO.ScheduleInfoDto;
import com.gdutelc.domain.query.ScheduleInfoQueryDto;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/1/28 23:44
 * ScheduleInfoService 课表Service
 */
public interface ScheduleInfoService {
    /**
     * 获得课表信息
     * @param queryDto 请求信息
     * @return Map
     */
    Map<String, ArrayList<ScheduleInfoDto>> getScheduleInfo(ScheduleInfoQueryDto queryDto);
}
