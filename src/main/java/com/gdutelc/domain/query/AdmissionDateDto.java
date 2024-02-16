package com.gdutelc.domain.query;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/2/16 16:49
 * AdmissionDateDto
 */
public class AdmissionDateDto {

    @NotBlank(message = "日期不能为空")
    private String admissionDate;

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Override
    public String toString() {
        return "AdmissionDateDto{" +
                "admissionDate='" + admissionDate + '\'' +
                '}';
    }
}
