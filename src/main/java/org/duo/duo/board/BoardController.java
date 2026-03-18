package org.duo.duo.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.constants.PageConstants;
import org.duo.duo.common.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String list(@ModelAttribute BoardSearchRequest request,
                       @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int page,
                       Model model) {
        PageRequest pageable = PageConstants.of(page, Sort.by("createdAt").descending());
        Page<BoardResponse> result = boardService.search(request, pageable);

        model.addAttribute("boards", result);
        model.addAttribute("search", request);
        model.addAttribute("boardTypes", BoardType.values());
        return "board-list";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("boardCreateRequest", new BoardCreateRequest());
        model.addAttribute("boardTypes", BoardType.values());
        return "board-write";
    }

    @PostMapping("/write")
    public String create(@Valid @ModelAttribute BoardCreateRequest boardCreateRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardTypes", BoardType.values());
            return "board-write";
        }
        boardService.create(boardCreateRequest, principal.getUser());
        return "redirect:/boards";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardResponse board = boardService.view(id);
        model.addAttribute("board", board);
        return "board-detail";
    }
}
