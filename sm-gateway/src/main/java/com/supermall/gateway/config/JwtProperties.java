package com.supermall.gateway.config;

import com.supermall.auth.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
@Slf4j
@ConfigurationProperties(prefix = "sm.jwt")
public class JwtProperties {
    private String pubKeyPath;//公钥路径
    private String cookieName; //cookie名称
    private PublicKey publicKey; // 公钥

    //对象实例化之后，获取公钥和私钥
    @PostConstruct
    public void init() throws Exception {
        //获取公私钥
        this.publicKey=RsaUtils.getPublicKey(pubKeyPath);
    }

    public JwtProperties() {
    }

    public JwtProperties(String pubKeyPath, String cookieName, PublicKey publicKey) {
        this.pubKeyPath = pubKeyPath;
        this.cookieName = cookieName;
        this.publicKey = publicKey;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
