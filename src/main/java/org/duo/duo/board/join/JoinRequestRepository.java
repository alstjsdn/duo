package org.duo.duo.board.join;

import org.duo.duo.board.GameLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByBoard_BoardIdAndStatus(Long boardId, JoinRequestStatus status);

    List<JoinRequest> findByBoard_BoardIdAndGameLineAndStatus(Long boardId, GameLine gameLine, JoinRequestStatus status);

    boolean existsByBoard_BoardIdAndUser_UserIdAndGameLineAndStatus(Long boardId, Long userId, GameLine gameLine, JoinRequestStatus status);

    boolean existsByBoard_BoardIdAndGameLineAndStatus(Long boardId, GameLine gameLine, JoinRequestStatus status);
}