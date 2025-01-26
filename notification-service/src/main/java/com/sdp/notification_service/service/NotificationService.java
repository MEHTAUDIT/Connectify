package com.sdp.notification_service.service;

import com.sdp.notification_service.entity.Notification;
import com.sdp.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<String> getUserNotifications(Long userId){

        List<Notification> notifications = notificationRepository.findByUserId(userId);

        List<String> messages = new ArrayList<>();

        for (Notification notification : notifications) {
            log.info("Notification: {}", notification);
            if (notification.getMessage() != null) {
                messages.add(notification.getMessage());
            }
        }

        return messages;
    }
}
