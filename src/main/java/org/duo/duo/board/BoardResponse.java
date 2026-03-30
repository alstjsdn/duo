package org.duo.duo.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long boardId;
    private String username;
    private String author;
    private String profileImage;
    private String bio;
    private BoardType type;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;

    public static BoardResponse from(Board board) {
        BoardResponse response = new BoardResponse();
        response.boardId = board.getBoardId();
        response.username = board.getUser().getUsername();
        response.author = board.getUser().getName();
        response.profileImage = board.getUser().getProfileImage();
        response.bio = board.getUser().getBio();
        response.type = board.getType();
        response.title = board.getTitle();
        response.content = board.getContent();
        response.viewCount = board.getViewCount();
        response.createdAt = board.getCreatedAt();
        return response;
    }
}