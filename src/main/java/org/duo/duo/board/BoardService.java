package org.duo.duo.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardResponse> search(BoardSearchRequest request, Pageable pageable) {
        Specification<Board> spec = Specification
                .where(BoardSpecification.hasType(request.getType()))
                .and(BoardSpecification.titleContains(request.getTitle()))
                .and(BoardSpecification.contentContains(request.getContent()));

        return boardRepository.findAll(spec, pageable)
                .map(BoardResponse::from);
    }

    @Transactional
    public void create(BoardCreateRequest request, User user) {
        Board board = Board.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        boardRepository.save(board);
    }

    @Transactional
    public BoardResponse view(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        board.increaseViewCount();
        return BoardResponse.from(board);
    }

}
