package com.supermall.order.intercepter;

import com.supermall.auth.pojo.UserInfo;
import com.supermall.auth.utils.JwtUtils;
import com.supermall.common.utils.CookieUtils;
import com.supermall.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    private JwtProperties prop;
    private static final ThreadLocal<UserInfo> tl=new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop=prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        if (StringUtils.isBlank(token)) {
            // 未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            //解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //传递userinfo
            tl.set(user);
            //放行
            return true;
        } catch (Exception e) {
            log.error("[购物车服务] 解析用户身份失败.",e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //最后用完数据，清空数据
        tl.remove();
    }
    public static UserInfo getUser(){
        return tl.get();
    }
}