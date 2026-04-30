package org.duo.duo.freeboard.comment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FreeBoardCommentResponse {

    private final Long id;
    private final String username;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
    private final List<FreeBoardCommentResponse> children;

    private FreeBoardCommentResponse(FreeBoardComment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.author = comment.getUser().getName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.children = comment.getChildren().stream()
                .map(FreeBoardCommentResponse::from)
                .collect(Collectors.toList());
    }

    public static FreeBoardCommentResponse from(FreeBoardComment comment) {
        return new FreeBoardCommentResponse(comment);
    }
}