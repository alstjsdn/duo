package org.duo.duo.notification.event;

import lombok.RequiredArgsConstructor;
import org.duo.duo.board.comment.Comment;
import org.duo.duo.notification.NotificationService;
import org.duo.duo.notification.NotificationType;
import org.duo.duo.user.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void onCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        User writer = comment.getUser();
        String url = "/boards/" + comment.getBoard().getBoardId();

        if (comment.getParent() != null) {
            // 대댓글 → 부모 댓글 작성자에게 알림
            User parentAuthor = comment.getParent().getUser();
            if (!parentAuthor.getUserId().equals(writer.getUserId())) {
                notificationService.send(parentAuthor, NotificationType.REPLY,
                        writer.getName() + "님이 답글을 달았습니다.", url);
            }
        } else {
            // 댓글 → 게시글 작성자에게 알림
            User boardOwner = comment.getBoard().getUser();
            if (!boardOwner.getUserId().equals(writer.getUserId())) {
                notificationService.send(boardOwner, NotificationType.COMMENT,
                        writer.getName() + "님이 댓글을 달았습니다.", url);
            }
        }
    }
}