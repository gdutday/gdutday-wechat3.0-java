package com.gdutelc.domain.query;

import com.gdutelc.domain.query.BaseRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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
    @NotNull(message = "termId不能为空")
    private Integer termId;
    @NotBlank(message = "cookie不能为空")
    private String cookies;
    @NotNull(message = "用户类型不能为空")
    @Range(max = 3, min = 0, message = "请输入正确的userType")
    private Integer userType;
}
