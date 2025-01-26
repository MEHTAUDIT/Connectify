package com.sdp.notification_service.clients;

import com.sdp.notification_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",path = "/users")
public interface UsersClient {

    @GetMapping("/user/{userId}")
    UserDto getUserById(@PathVariable Long userId);

    @GetMapping("/user-exists/{userId}")
    boolean checkUserExists(@PathVariable Long userId);
}
