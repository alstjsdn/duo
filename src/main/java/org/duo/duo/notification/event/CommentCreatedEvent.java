package org.duo.duo.notification.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.comment.Comment;

@Getter
@RequiredArgsConstructor
public class CommentCreatedEvent {
    private final Comment comment;
}