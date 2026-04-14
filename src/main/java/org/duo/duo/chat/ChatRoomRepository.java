package org.duo.duo.chat;

import org.duo.duo.board.join.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByBoard_BoardId(Long boardId);

    @Query("""
        SELECT cr FROM ChatRoom cr
        JOIN FETCH cr.board b
        JOIN FETCH b.user
        WHERE b.user.userId = :userId
           OR EXISTS (
               SELECT jr FROM JoinRequest jr
               WHERE jr.board = b
                 AND jr.user.userId = :userId
                 AND jr.status = :status
           )
        ORDER BY cr.createdAt DESC
        """)
    List<ChatRoom> findAccessibleByUserId(@Param("userId") Long userId,
                                          @Param("status") JoinRequestStatus status);
}
