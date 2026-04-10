package org.duo.duo.board;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private BoardStatus status;
    private GameLine myLine;
    private Integer memberCount;
    private List<GameLine> neededLines;

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
        response.status = board.getStatus();
        response.myLine = board.getMyLine();
        response.memberCount = board.getMemberCount();
        response.neededLines = new java.util.ArrayList<>(board.getNeededLines());
        return response;
    }
}