package com.supermall.auth.web;

import com.supermall.auth.config.JwtProperties;
import com.supermall.auth.pojo.UserInfo;
import com.supermall.auth.service.AuthService;
import com.supermall.auth.utils.JwtUtils;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.utils.CookieUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableConfigurationProperties(JwtProperties.class)
@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    private JwtProperties prop;
    /**
     * 登录验证
     * @param username
     * @param password
     * @param response
     * @param request
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void>  login(@RequestParam("username") String username, @RequestParam("password") String  password,
                                        HttpServletResponse response, HttpServletRequest request){
        //登录
        String token = authService.login(username,password);
        //写入cookie
        CookieUtils.newBuilder(response).httpOnly().request(request)
                .build(prop.getCookieName(),token);
//        CookieUtils.newBuilder(response).httpOnly().request(request)
//                .build(cookieName,token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("SM_TOKEN") String token, HttpServletResponse response, HttpServletRequest request){
        try{
            //解释token
            UserInfo userinfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //刷新token
            String newToken = JwtUtils.generateToken(userinfo, prop.getPrivateKey(), prop.getExpire());
            //写入cookie
            CookieUtils.newBuilder(response).httpOnly().request(request).build(prop.getCookieName(),newToken);
            //已登录，返回用户信息
            return ResponseEntity.ok(userinfo);
        }catch (Exception e){
            throw new SmException(ExceptionEnum.INVAID_FILE_TYPE);
        }
    }
}
