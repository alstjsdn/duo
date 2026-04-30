package org.duo.duo.freeboard.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.freeboard.FreeBoard;
import org.duo.duo.freeboard.FreeBoardRepository;
import org.duo.duo.user.Role;
import org.duo.duo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardCommentService {

    private final FreeBoardCommentRepository commentRepository;
    private final FreeBoardRepository freeBoardRepository;

    @Transactional
    public void create(User user, Long boardId, Long parentId, FreeBoardCommentRequest request) {
        FreeBoard board = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        FreeBoardComment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        }
        commentRepository.save(FreeBoardComment.builder()
                .freeBoard(board)
                .user(user)
                .parent(parent)
                .content(request.getContent())
                .build());
    }

    @Transactional
    public void update(Long id, FreeBoardCommentRequest request, User user) {
        FreeBoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        comment.update(request.getContent());
    }

    @Transactional
    public void delete(Long id, User user) {
        FreeBoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    public Page<FreeBoardCommentResponse> findByBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findByFreeBoard_IdAndParentIsNull(boardId, pageable)
                .map(FreeBoardCommentResponse::from);
    }
}