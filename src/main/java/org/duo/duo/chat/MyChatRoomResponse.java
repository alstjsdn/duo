package org.duo.duo.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyChatRoomResponse {

    private Long boardId;
    private Long chatRoomId;
    private String title;
    private String boardTypeName;
    private String boardType;
    private String boardStatusName;
    private String boardStatus;
    private String role;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static MyChatRoomResponse of(ChatRoom room, boolean isOwner, ChatMessage lastMsg) {
        return new MyChatRoomResponse(
                room.getBoard().getBoardId(),
                room.getId(),
                room.getBoard().getTitle(),
                room.getBoard().getType().getName(),
                room.getBoard().getType().name(),
                room.getBoard().getStatus().getName(),
                room.getBoard().getStatus().name(),
                isOwner ? "방장" : "멤버",
                lastMsg != null ? lastMsg.getContent() : null,
                lastMsg != null ? lastMsg.getCreatedAt() : null
        );
    }
}
