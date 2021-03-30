package com.alex.wx.hualuo.config;

//import lombok.Data;
//import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/16 18:27
 */
@ConfigurationProperties(prefix = "wx.mp.config")
public class WxMpProperties {

    private String appId;

    private String secret;

    private String token;

    private String aesKey;

    private boolean redisEnabled;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public boolean isRedisEnabled() {
        return redisEnabled;
    }

    public void setRedisEnabled(boolean redisEnabled) {
        this.redisEnabled = redisEnabled;
    }
}
