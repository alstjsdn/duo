package org.duo.duo.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrollReportRepository extends JpaRepository<TrollReport, Long> {

    @Query(value = "SELECT r FROM TrollReport r JOIN FETCH r.reporter JOIN FETCH r.reported LEFT JOIN FETCH r.board",
           countQuery = "SELECT COUNT(r) FROM TrollReport r")
    Page<TrollReport> findAllWithUsers(Pageable pageable);

    boolean existsByReporter_UserIdAndReported_UserId(Long reporterId, Long reportedId);
}