package org.duo.duo.board.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long commentId;
    private String username;
    private String author;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.username = comment.getUser().getUsername();
        response.author = comment.getUser().getName();
        response.content = comment.getContent();
        response.createdAt = comment.getCreatedAt();
        return response;
    }
}