package org.duo.duo.chat;

import jakarta.persistence.*;
import lombok.*;
import org.duo.duo.board.Board;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
