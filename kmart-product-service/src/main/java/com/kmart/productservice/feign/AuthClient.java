package com.kmart.productservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/users/role")  // ✅ correct endpoint
    String getRoleFromToken(@RequestHeader("Authorization") String token);
}
