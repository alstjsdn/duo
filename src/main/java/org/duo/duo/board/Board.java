package org.duo.duo.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duo.duo.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.duo.duo.board.comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private BoardType type;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private int viewCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BoardStatus status = BoardStatus.RECRUITING;

    @Enumerated(EnumType.STRING)
    @Column
    private GameLine myLine;

    @Column
    private Integer memberCount;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "board_needed_lines", joinColumns = @JoinColumn(name = "board_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "line")
    private List<GameLine> neededLines = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateStatus(BoardStatus boardStatus) {
        this.status = boardStatus;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(BoardRequest request) {
        this.type = request.getType();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.myLine = request.getMyLine();
        this.memberCount = request.getMemberCount();
        this.neededLines.clear();
        if (request.getNeededLines() != null) {
            this.neededLines.addAll(request.getNeededLines());
        }
    }

    public static Board toEntity(BoardRequest request, User user) {
        Board board = Board.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .myLine(request.getMyLine())
                .memberCount(request.getMemberCount())
                .viewCount(0)
                .build();
        if (request.getNeededLines() != null) {
            board.neededLines.addAll(request.getNeededLines());
        }
        return board;
    }
}
