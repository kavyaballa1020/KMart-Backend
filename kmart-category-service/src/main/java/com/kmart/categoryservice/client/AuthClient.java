package com.kmart.categoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/admin/api/users/role") // Match actual full path
    String getUserRole(@RequestHeader("Authorization") String token);
}
