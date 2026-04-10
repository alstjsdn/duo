package org.duo.duo.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.constants.PageConstants;
import org.duo.duo.common.service.ImageService;
import org.duo.duo.user.Role;
import org.duo.duo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;

    public Page<BoardResponse> search(BoardSearchRequest request, Pageable pageable) {
        Specification<Board> spec = Specification
                .where(BoardSpecification.hasType(request.getType()))
                .and(BoardSpecification.titleContains(request.getTitle()))
                .and(BoardSpecification.contentContains(request.getContent()))
                .and(BoardSpecification.neededLineContains(request.getNeededLine()));

        return boardRepository.findAll(spec, pageable)
                .map(BoardResponse::from);
    }

    @Transactional
    public void create(BoardRequest request, User user) {
        request.setContent(imageService.processBase64Images(request.getContent()));
        Board board = Board.toEntity(request, user);
        boardRepository.save(board);
    }

    @Transactional
    public BoardResponse view(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        board.increaseViewCount();
        return BoardResponse.from(board);
    }

    public BoardResponse get(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        return BoardResponse.from(board);
    }

    @Transactional
    public void update(Long id, BoardRequest request, User user) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        request.setContent(imageService.processBase64Images(request.getContent()));
        board.update(request);
    }

    @Transactional
    public void delete(Long id, User user) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        boardRepository.delete(board);
    }

    public Page<BoardResponse> findMyboard(User user) {

        PageRequest pageable = PageConstants.of(Integer.parseInt(PageConstants.DEFAULT_PAGE), Sort.by("createdAt").descending());
        return boardRepository.findByUser_UserId(user.getUserId(), pageable).map(BoardResponse::from);
    }
}
