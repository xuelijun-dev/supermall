package com.supermall.sms.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sm.sms")
public class SmsProperties {
    private String  accessKeyId;
    private String accessKeySecret;
    private String  signName;
    private String  templateCode;
}
