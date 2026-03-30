package org.duo.duo.board.comment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponse {
    private Long commentId;
    private Long boardId;
    private String boardTitle;
    private String username;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponse> children;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.boardId = comment.getBoard().getBoardId();
        response.boardTitle = comment.getBoard().getTitle();
        response.username = comment.getUser().getUsername();
        response.author = comment.getUser().getName();
        response.content = comment.getContent();
        response.createdAt = comment.getCreatedAt();
        response.children = comment.getChildren().stream().map(CommentResponse::from).toList();
        return response;
    }
}