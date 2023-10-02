package com.gdutelc.domain.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 12:07
 * Examination 考试安排
 */
@Getter
@Setter
@ToString
public class ExaminationDto implements Serializable {

    private String address;

    private String sort;

    private Integer id;

    private String time;

    private String type;

    private String date;

    private String campus;

    private String clazzName;

    public ExaminationDto(String address, String sort, Integer id, String time, String type, String date, String campus, String clazzName) {
        this.address = address;
        this.sort = sort;
        this.id = id;
        this.time = time;
        this.type = type;
        this.date = date;
        this.campus = campus;
        this.clazzName = clazzName;
    }
}
