package com.supermall.auth.client;

import com.supermall.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient  extends UserApi {
}
