package org.duo.duo.freeboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, JpaSpecificationExecutor<FreeBoard> {
    Page<FreeBoard> findByUser_UserId(Long userId, Pageable pageable);
}