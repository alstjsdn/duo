package org.duo.duo.chat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long id;
    private String senderName;
    private String senderUsername;
    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .senderName(message.getSender().getName())
                .senderUsername(message.getSender().getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
