package com.gdutelc.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:18
 * GdutConfig
 */
@Component
@ConfigurationProperties(prefix = "gdutday")
public class GdutConfig {
    private String name;
    private String version;
    private String copyRightYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyRightYear() {
        return copyRightYear;
    }

    public void setCopyRightYear(String copyRightYear) {
        this.copyRightYear = copyRightYear;
    }
}
