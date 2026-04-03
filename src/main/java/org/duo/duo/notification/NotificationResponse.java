package org.duo.duo.notification;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private Long notificationId;
    private String type;
    private String message;
    private String url;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.notificationId = notification.getNotificationId();
        response.type = notification.getType().getName();
        response.message = notification.getMessage();
        response.url = notification.getUrl();
        response.isRead = notification.isRead();
        response.createdAt = notification.getCreatedAt();
        return response;
    }
}