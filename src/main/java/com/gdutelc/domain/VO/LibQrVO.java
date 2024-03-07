package com.gdutelc.domain.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 12:03
 * LibQrVO 图书馆二维码
 */
@Getter
@Setter
@ToString
public class LibQrVO implements Serializable {

    //"学号")
    private String stuId;
    // "QR宽度")
    private Integer widthStr;
    // "QR高度")
    private Integer heightStr;

    public LibQrVO(String stuId, Integer widthStr, Integer heightStr) {
        this.stuId = stuId;
        this.widthStr = widthStr;
        this.heightStr = heightStr;
    }
}
