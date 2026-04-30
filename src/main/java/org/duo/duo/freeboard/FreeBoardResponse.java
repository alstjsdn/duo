package org.duo.duo.freeboard;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FreeBoardResponse {

    private final Long id;
    private final String username;
    private final String author;
    private final String profileImage;
    private final String bio;
    private final FreeBoardType type;
    private final String title;
    private final String content;
    private final int viewCount;
    private final LocalDateTime createdAt;

    private FreeBoardResponse(FreeBoard board) {
        this.id = board.getId();
        this.username = board.getUser().getUsername();
        this.author = board.getUser().getName();
        this.profileImage = board.getUser().getProfileImage();
        this.bio = board.getUser().getBio();
        this.type = board.getType();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        this.createdAt = board.getCreatedAt();
    }

    public static FreeBoardResponse from(FreeBoard board) {
        return new FreeBoardResponse(board);
    }
}