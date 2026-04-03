package org.duo.duo.notification;

import lombok.RequiredArgsConstructor;
import org.duo.duo.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitters sseEmitters;

    public SseEmitter subscribe(Long userId) {
        return sseEmitters.add(userId);
    }

    @Transactional
    public void send(User receiver, NotificationType type, String message, String url) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .type(type)
                .message(message)
                .url(url)
                .build();
        notificationRepository.save(notification);

        sseEmitters.send(receiver.getUserId(), NotificationResponse.from(notification));
    }

    public List<NotificationResponse> getUnread(Long userId) {
        return notificationRepository.findByReceiver_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream().map(NotificationResponse::from).toList();
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByReceiver_UserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(Notification::markRead);
    }

    @Transactional
    public void markAllRead(Long userId) {
        notificationRepository.findByReceiver_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .forEach(Notification::markRead);
    }
}