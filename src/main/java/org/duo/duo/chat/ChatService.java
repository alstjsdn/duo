package org.duo.duo.chat;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.Board;
import org.duo.duo.board.BoardRepository;
import org.duo.duo.board.join.JoinRequestRepository;
import org.duo.duo.board.join.JoinRequestStatus;
import org.duo.duo.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BoardRepository boardRepository;
    private final JoinRequestRepository joinRequestRepository;

    @Transactional
    public ChatRoom createChatRoom(Board board) {
        return chatRoomRepository.save(ChatRoom.builder().board(board).build());
    }

    public ChatRoom getChatRoomByBoardId(Long boardId) {
        return chatRoomRepository.findByBoard_BoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    public boolean hasAccess(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return false;
        if (board.getUser().getUserId().equals(userId)) return true;
        return joinRequestRepository.existsByBoard_BoardIdAndUser_UserIdAndStatus(
                boardId, userId, JoinRequestStatus.APPROVED);
    }

    @Transactional
    public ChatMessageResponse saveMessage(Long chatRoomId, User sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();
        return ChatMessageResponse.from(chatMessageRepository.save(message));
    }

    public List<ChatMessageResponse> getMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(chatRoomId)
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}
