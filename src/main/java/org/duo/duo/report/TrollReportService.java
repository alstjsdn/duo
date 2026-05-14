package org.duo.duo.report;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.Board;
import org.duo.duo.board.BoardRepository;
import org.duo.duo.board.join.JoinRequest;
import org.duo.duo.board.join.JoinRequestRepository;
import org.duo.duo.board.join.JoinRequestStatus;
import org.duo.duo.user.User;
import org.duo.duo.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrollReportService {

    private final TrollReportRepository trollReportRepository;
    private final UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final BoardRepository boardRepository;

    private static final List<JoinRequestStatus> MEMBER_STATUSES =
            List.of(JoinRequestStatus.APPROVED, JoinRequestStatus.KICKED);

    @Transactional
    public void report(User reporter, TrollReportRequest request) {
        User reported = userRepository.findByRiotIdAndRiotTag(request.getRiotId(), request.getRiotTag())
                .orElseThrow(() -> new EntityNotFoundException("해당 라이엇 계정을 가진 유저를 찾을 수 없습니다."));

        if (reporter.getUserId().equals(reported.getUserId())) {
            throw new IllegalArgumentException("본인을 신고할 수 없습니다.");
        }

        if (trollReportRepository.existsByReporter_UserIdAndReported_UserId(
                reporter.getUserId(), reported.getUserId())) {
            throw new IllegalStateException("이미 신고한 유저입니다.");
        }

        Board commonBoard = findRecentCommonBoard(reporter.getUserId(), reported.getUserId());
        if (commonBoard == null) {
            throw new IllegalStateException("최근 30일 이내 함께 파티를 진행한 유저만 신고할 수 있습니다.");
        }

        trollReportRepository.save(TrollReport.builder()
                .reporter(reporter)
                .reported(reported)
                .board(commonBoard)
                .content(request.getContent())
                .build());
    }

    public Page<TrollReportResponse> getReports(Pageable pageable) {
        return trollReportRepository.findAllWithUsers(pageable).map(TrollReportResponse::from);
    }

    public List<RecentMemberDto> getRecentPartyMembers(User reporter) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Set<Long> seen = new HashSet<>();
        List<RecentMemberDto> result = new ArrayList<>();

        for (Board board : boardRepository.findByUser_UserIdAndCreatedAtAfter(reporter.getUserId(), thirtyDaysAgo)) {
            for (JoinRequest jr : joinRequestRepository.findByBoard_BoardIdAndStatusIn(board.getBoardId(), MEMBER_STATUSES)) {
                User u = jr.getUser();
                if (!u.getUserId().equals(reporter.getUserId()) && seen.add(u.getUserId())) {
                    result.add(RecentMemberDto.from(u));
                }
            }
        }

        for (JoinRequest jr : joinRequestRepository.findByUser_UserIdAndStatusInAndBoard_CreatedAtAfter(
                reporter.getUserId(), MEMBER_STATUSES, thirtyDaysAgo)) {
            Board board = jr.getBoard();
            User host = board.getUser();
            if (!host.getUserId().equals(reporter.getUserId()) && seen.add(host.getUserId())) {
                result.add(RecentMemberDto.from(host));
            }
            for (JoinRequest m : joinRequestRepository.findByBoard_BoardIdAndStatusIn(board.getBoardId(), MEMBER_STATUSES)) {
                User u = m.getUser();
                if (!u.getUserId().equals(reporter.getUserId()) && seen.add(u.getUserId())) {
                    result.add(RecentMemberDto.from(u));
                }
            }
        }

        return result;
    }

    private Board findRecentCommonBoard(Long reporterId, Long reportedId) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        // 신고자가 방장인 파티에 피신고자가 참여했는지 확인
        for (Board board : boardRepository.findByUser_UserIdAndCreatedAtAfter(reporterId, thirtyDaysAgo)) {
            if (joinRequestRepository.existsByBoard_BoardIdAndUser_UserIdAndStatusIn(
                    board.getBoardId(), reportedId, MEMBER_STATUSES)) {
                return board;
            }
        }

        // 신고자가 파티원이었던 경우, 피신고자가 방장이거나 동일 파티원인지 확인
        List<JoinRequest> reporterJoined = joinRequestRepository
                .findByUser_UserIdAndStatusInAndBoard_CreatedAtAfter(reporterId, MEMBER_STATUSES, thirtyDaysAgo);
        for (JoinRequest jr : reporterJoined) {
            Board board = jr.getBoard();
            if (board.getUser().getUserId().equals(reportedId)) {
                return board;
            }
            if (joinRequestRepository.existsByBoard_BoardIdAndUser_UserIdAndStatusIn(
                    board.getBoardId(), reportedId, MEMBER_STATUSES)) {
                return board;
            }
        }

        return null;
    }
}