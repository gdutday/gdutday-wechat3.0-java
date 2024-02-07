package com.gdutelc.domain.query;

import com.gdutelc.domain.query.BaseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2024/02/07/16:45
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExaminationReqDto {
    private String termId;
    private String cookies;
    private Integer userType;
}
