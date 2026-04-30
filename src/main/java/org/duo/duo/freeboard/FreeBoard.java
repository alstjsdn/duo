package org.duo.duo.freeboard;

import jakarta.persistence.*;
import lombok.*;
import org.duo.duo.freeboard.comment.FreeBoardComment;
import org.duo.duo.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "free_board")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FreeBoardType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FreeBoardComment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(FreeBoardRequest request) {
        this.type = request.getType();
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public static FreeBoard toEntity(FreeBoardRequest request, User user) {
        return FreeBoard.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}