package org.duo.duo.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {

    Board findByBoardId(Long boardId);

    Page<Board> findByUser_UserId(Long userId, PageRequest pageable);

    List<Board> findByUser_UserIdAndCreatedAtAfter(Long userId, LocalDateTime after);
}
