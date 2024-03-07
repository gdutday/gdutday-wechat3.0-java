package com.gdutelc.domain.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author Ymri
 * @version 1.0
 * @since 2024/1/28 21:34
 * ScheduleInfoQueryDto 课表请求接口
 */

@ToString
@Setter
@Getter
public class ScheduleInfoQueryDto extends BaseRequestDto {


    private Integer termId;

    public ScheduleInfoQueryDto(String cookies, Integer userType, Integer termId) {
        super(cookies, userType);
        this.termId = termId;
    }
}
