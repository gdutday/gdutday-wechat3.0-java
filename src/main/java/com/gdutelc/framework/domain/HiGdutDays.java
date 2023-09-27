package com.gdutelc.framework.domain;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:55
 * HiGdutDays,Test requests show
 */
public class HiGdutDays implements Serializable {
    private String Hi;

    public HiGdutDays(String name) {
        this.Hi = name;
    }


    public String getHi() {
        return Hi;
    }

    public void setHi(String name) {
        this.Hi = name;
    }

    @Override
    public String toString() {
        return "HiGdutDays{" +
                "Hi='" + Hi + '\'' +
                '}';
    }
}
