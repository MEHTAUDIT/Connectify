package com.sdp.notification_service.controller;

import com.sdp.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping
    public List<String> getUserNotifications(@RequestHeader("X-User-Id") Long userId) {
        //Get all messages for the user with userId
        return notificationService.getUserNotifications(userId);
    }
}
