package org.duo.duo.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiver_UserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    long countByReceiver_UserIdAndIsReadFalse(Long userId);
}