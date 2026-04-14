package org.duo.duo.chat;

import lombok.RequiredArgsConstructor;
import org.duo.duo.common.security.UserPrincipal;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat/rooms")
    public String myChatRooms(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        if (principal == null) return "redirect:/login";
        model.addAttribute("rooms", chatService.getMyChatRooms(principal.getUser().getUserId()));
        return "chat-rooms";
    }

    @GetMapping("/chat/{boardId}")
    public String chatRoom(@PathVariable Long boardId,
                           @AuthenticationPrincipal UserPrincipal principal,
                           Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        if (!chatService.hasAccess(boardId, principal.getUser().getUserId())) {
            throw new AccessDeniedException("채팅방 접근 권한이 없습니다.");
        }
        ChatRoom chatRoom = chatService.getChatRoomByBoardId(boardId);
        List<ChatMessageResponse> messages = chatService.getMessages(chatRoom.getId());

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("messages", messages);
        model.addAttribute("boardId", boardId);
        model.addAttribute("currentUsername", principal.getUsername());
        model.addAttribute("currentName", principal.getUser().getName());
        return "chat";
    }

    @MessageMapping("/chat/{boardId}/send")
    public void sendMessage(@DestinationVariable Long boardId,
                            @Payload ChatMessageRequest request,
                            Principal principal) {
        Authentication auth = (Authentication) principal;
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        if (!chatService.hasAccess(boardId, userPrincipal.getUser().getUserId())) {
            throw new AccessDeniedException("채팅방 접근 권한이 없습니다.");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            return;
        }

        ChatRoom chatRoom = chatService.getChatRoomByBoardId(boardId);
        ChatMessageResponse response = chatService.saveMessage(
                chatRoom.getId(),
                userPrincipal.getUser(),
                request.getContent().trim()
        );
        messagingTemplate.convertAndSend("/topic/chat/" + boardId, response);
    }
}
