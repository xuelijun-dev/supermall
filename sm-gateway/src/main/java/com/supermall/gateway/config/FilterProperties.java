package com.supermall.gateway.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("sm.filter")
public class FilterProperties {
    private List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }

    public FilterProperties(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }

    public FilterProperties() {
    }
}
