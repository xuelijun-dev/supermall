package com.supermall.auth.config;

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
    private String secret; // 密钥
    private String pubKeyPath;// 公钥
    private String priKeyPath;// 私钥
    private int expire;// token过期时间
    private PublicKey publicKey; // 公钥
    private PrivateKey privateKey; // 私钥
    private String cookieName;

    @PostConstruct
    public void init(){
        try{
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if(!pubKey.exists() || !priKey.exists()){
                RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
            }
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);

         }catch (Exception e) {
        log.error("初始化公钥和私钥失败！", e);
        throw new RuntimeException();
    }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public JwtProperties() {
    }

    public JwtProperties(String secret, String pubKeyPath, String priKeyPath, int expire, PublicKey publicKey, PrivateKey privateKey, String cookieName) {
        this.secret = secret;
        this.pubKeyPath = pubKeyPath;
        this.priKeyPath = priKeyPath;
        this.expire = expire;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.cookieName = cookieName;
    }
}
