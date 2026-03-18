package org.duo.duo.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long boardId;
    private String author;
    private BoardType type;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;

    public static BoardResponse from(Board board) {
        BoardResponse response = new BoardResponse();
        response.boardId = board.getBoardId();
        response.author = board.getUser().getName();
        response.type = board.getType();
        response.title = board.getTitle();
        response.content = board.getContent();
        response.viewCount = board.getViewCount();
        response.createdAt = board.getCreatedAt();
        return response;
    }
}