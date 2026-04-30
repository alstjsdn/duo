package org.duo.duo.freeboard.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
    Page<FreeBoardComment> findByFreeBoard_IdAndParentIsNull(Long boardId, Pageable pageable);
}