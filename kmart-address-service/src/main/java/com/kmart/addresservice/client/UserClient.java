package com.kmart.addresservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/api/users/id")
    Long getUserId(@RequestHeader("Authorization") String token);
}

