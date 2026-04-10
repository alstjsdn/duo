package org.duo.duo.board.join;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.Board;
import org.duo.duo.board.BoardRepository;
import org.duo.duo.board.BoardStatus;
import org.duo.duo.board.GameLine;
import org.duo.duo.notification.NotificationService;
import org.duo.duo.notification.NotificationType;
import org.duo.duo.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    public Map<GameLine, JoinRequestResponse> getApprovedByLine(Long boardId) {
        return joinRequestRepository.findByBoard_BoardIdAndStatus(boardId, JoinRequestStatus.APPROVED)
                .stream()
                .collect(Collectors.toMap(JoinRequest::getGameLine, JoinRequestResponse::from));
    }

    public List<JoinRequestResponse> getPendingRequests(Long boardId) {
        return joinRequestRepository.findByBoard_BoardIdAndStatus(boardId, JoinRequestStatus.PENDING)
                .stream()
                .map(JoinRequestResponse::from)
                .collect(Collectors.toList());
    }

    public boolean hasApprovedRequests(Long boardId) {
        return !joinRequestRepository.findByBoard_BoardIdAndStatus(boardId, JoinRequestStatus.APPROVED).isEmpty();
    }

    public Set<GameLine> getMyPendingLines(Long boardId, Long userId) {
        return joinRequestRepository.findByBoard_BoardIdAndStatus(boardId, JoinRequestStatus.PENDING)
                .stream()
                .filter(r -> r.getUser().getUserId().equals(userId))
                .map(JoinRequest::getGameLine)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void requestJoin(Long boardId, GameLine gameLine, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (board.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인 게시글에는 참가 요청할 수 없습니다.");
        }
        if (joinRequestRepository.existsByBoard_BoardIdAndGameLineAndStatus(boardId, gameLine, JoinRequestStatus.APPROVED)) {
            throw new IllegalStateException("이미 해당 라인이 채워졌습니다.");
        }
        if (joinRequestRepository.existsByBoard_BoardIdAndUser_UserIdAndGameLineAndStatus(boardId, user.getUserId(), gameLine, JoinRequestStatus.PENDING)) {
            throw new IllegalStateException("이미 참가 요청을 보냈습니다.");
        }

        joinRequestRepository.save(JoinRequest.builder()
                .board(board)
                .user(user)
                .gameLine(gameLine)
                .status(JoinRequestStatus.PENDING)
                .build());

        notificationService.send(
                board.getUser(),
                NotificationType.JOIN_REQUEST,
                user.getName() + "님이 [" + gameLine.getName() + "] 라인으로 참가 요청을 보냈습니다.",
                "/boards/" + boardId
        );
    }

    @Transactional
    public void approve(Long boardId, Long requestId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("승인 권한이 없습니다.");
        }

        JoinRequest target = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("요청을 찾을 수 없습니다."));

        // 같은 라인 다른 요청은 자동 거절
        joinRequestRepository.findByBoard_BoardIdAndGameLineAndStatus(boardId, target.getGameLine(), JoinRequestStatus.PENDING)
                .forEach(r -> {
                    if (!r.getId().equals(requestId)) {
                        r.reject();
                        notificationService.send(
                                r.getUser(),
                                NotificationType.JOIN_REJECTED,
                                "[" + r.getGameLine().getName() + "] 참가 요청이 거절되었습니다.",
                                "/boards/" + boardId
                        );
                    }
                });

        target.approve();
        notificationService.send(
                target.getUser(),
                NotificationType.JOIN_APPROVED,
                "[" + target.getGameLine().getName() + "] 참가 요청이 승인되었습니다!",
                "/boards/" + boardId
        );

        // 필요 라인이 모두 승인됐으면 모집완료로 변경
        if (board.getNeededLines() != null && !board.getNeededLines().isEmpty()) {
            Set<GameLine> approvedLines = joinRequestRepository
                    .findByBoard_BoardIdAndStatus(boardId, JoinRequestStatus.APPROVED)
                    .stream()
                    .map(JoinRequest::getGameLine)
                    .collect(Collectors.toSet());
            if (approvedLines.containsAll(board.getNeededLines())) {
                board.updateStatus(BoardStatus.COMPLETED);
            }
        }
    }

    @Transactional
    public void reject(Long boardId, Long requestId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("승인 권한이 없습니다.");
        }

        JoinRequest target = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("요청을 찾을 수 없습니다."));

        target.reject();
        notificationService.send(
                target.getUser(),
                NotificationType.JOIN_REJECTED,
                "[" + target.getGameLine().getName() + "] 참가 요청이 거절되었습니다.",
                "/boards/" + boardId
        );
    }
}