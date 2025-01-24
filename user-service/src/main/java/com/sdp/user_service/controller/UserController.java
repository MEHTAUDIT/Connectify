package com.sdp.user_service.controller;

import com.sdp.user_service.dto.UserDto;
import com.sdp.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {

        return userService.getUserById(userId);
    }

    @GetMapping("/user-exists/{userId}")
    public boolean checkUserExists(@PathVariable Long userId) {
        return userService.checkUserExists(userId);
    }
}
