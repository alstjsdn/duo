package org.duo.duo.freeboard;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.service.ImageService;
import org.duo.duo.user.Role;
import org.duo.duo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final ImageService imageService;

    public Page<FreeBoardResponse> search(FreeBoardSearchRequest request, Pageable pageable) {
        Specification<FreeBoard> spec = Specification
                .where(FreeBoardSpecification.hasType(request.getType()))
                .and(FreeBoardSpecification.titleContains(request.getTitle()))
                .and(FreeBoardSpecification.contentContains(request.getContent()));
        return freeBoardRepository.findAll(spec, pageable).map(FreeBoardResponse::from);
    }

    @Transactional
    public void create(FreeBoardRequest request, User user) {
        request.setContent(imageService.processBase64Images(request.getContent()));
        freeBoardRepository.save(FreeBoard.toEntity(request, user));
    }

    @Transactional
    public FreeBoardResponse view(Long id) {
        FreeBoard board = freeBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        board.increaseViewCount();
        return FreeBoardResponse.from(board);
    }

    public FreeBoardResponse get(Long id) {
        return FreeBoardResponse.from(freeBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다.")));
    }

    @Transactional
    public void update(Long id, FreeBoardRequest request, User user) {
        FreeBoard board = freeBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        request.setContent(imageService.processBase64Images(request.getContent()));
        board.update(request);
    }

    @Transactional
    public void delete(Long id, User user) {
        FreeBoard board = freeBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        freeBoardRepository.delete(board);
    }
}