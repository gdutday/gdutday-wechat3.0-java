package com.gdutelc.domain.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2026/4/15
 * CetScoreDto 英语考级成绩
 */
@Getter
@Setter
@ToString
public class CetScoreDto implements Serializable {

    /**
     * 学年学期
     */
    private String term;

    /**
     * 考级成绩（英语四级/英语六级）
     */
    private String cetLevel;

    /**
     * 总成绩
     */
    private String totalScore;

    /**
     * 准考证号
     */
    private String ticketNo;

    /**
     * 分项成绩1
     */
    private String part1Score;

    /**
     * 分项成绩2
     */
    private String part2Score;

    /**
     * 分项成绩3
     */
    private String part3Score;
}
