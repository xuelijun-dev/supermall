package com.supermall.user.api;

import com.supermall.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    User queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );
}
