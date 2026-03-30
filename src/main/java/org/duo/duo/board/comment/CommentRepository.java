package org.duo.duo.board.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoard_BoardIdAndParentIsNull(Long boardId, Pageable pageable);
    Page<Comment> findByUser_UserId(Long userId, Pageable pageable);
}
