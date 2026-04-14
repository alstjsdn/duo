package org.duo.duo.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_IdOrderByCreatedAtAsc(Long chatRoomId);

    Optional<ChatMessage> findTopByChatRoom_IdOrderByCreatedAtDesc(Long chatRoomId);
}
