package org.duo.duo.board.comment;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.Board;
import org.duo.duo.board.BoardRepository;
import org.duo.duo.board.BoardResponse;
import org.duo.duo.common.constants.PageConstants;
import org.duo.duo.notification.event.CommentCreatedEvent;
import org.duo.duo.user.Role;
import org.duo.duo.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Page<CommentResponse> findByBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findByBoard_BoardIdAndParentIsNull(boardId, pageable)
                .map(CommentResponse::from);
    }

    public void create(User user, Long boardId, Long parentId, CommentRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Comment parent = (parentId != null)
                ? commentRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."))
                : null;
        Comment comment = commentRepository.save(Comment.builder()
                .board(board)
                .user(user)
                .parent(parent)
                .content(request.getContent())
                .build());

        eventPublisher.publishEvent(new CommentCreatedEvent(comment));
    }

    @Transactional
    public void update(Long id, CommentRequest request,User user){

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        comment.update(request);
    }

    public void delete(Long id,User user){

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    public Page<CommentResponse> findMyComment(User user) {

        PageRequest pageable = PageConstants.of(Integer.parseInt(PageConstants.DEFAULT_PAGE), Sort.by("createdAt").descending());
        return commentRepository.findByUser_UserId(user.getUserId(), pageable).map(CommentResponse::from);
    }
}