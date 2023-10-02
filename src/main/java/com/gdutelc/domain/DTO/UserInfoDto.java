package com.gdutelc.domain.DTO;

import com.gdutelc.domain.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:16
 * UserInfoDto
 */
@Getter
@Setter
@ToString
public class UserInfoDto extends UserInfo implements Serializable {

    @ApiModelProperty("学期信息，研究生端需要使用")
    private String semester;

    public UserInfoDto(Long userName, String userNum, String departmentId, String departmentName, String departmenEnName, String semester) {
        super(userName, userNum, departmentId, departmentName, departmenEnName);
        this.semester = semester;
    }
}
