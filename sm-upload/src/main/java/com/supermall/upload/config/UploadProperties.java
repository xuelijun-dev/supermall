package com.supermall.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "sm.upload")
public class UploadProperties {
    private List<String>  allowTypes;
    private String baseUrl;

}
