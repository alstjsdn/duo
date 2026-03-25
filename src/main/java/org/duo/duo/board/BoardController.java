package org.duo.duo.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.comment.CommentService;
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
    private final CommentService commentService;

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
        model.addAttribute("boardRequest", new BoardRequest());
        model.addAttribute("boardTypes", BoardType.values());
        return "board-write";
    }

    @PostMapping("/write")
    public String create(@Valid @ModelAttribute BoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardTypes", BoardType.values());
            return "board-write";
        }
        boardService.create(boardRequest, principal.getUser());
        return "redirect:/boards";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int commentPage,
                         Model model) {
        BoardResponse board = boardService.view(id);
        PageRequest pageable = PageConstants.of(commentPage, Sort.by("createdAt").ascending());
        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.findByBoardId(id, pageable));
        return "board-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("boardId", id);
        model.addAttribute("boardRequest", BoardRequest.from(boardService.get(id)));
        model.addAttribute("boardTypes", BoardType.values());
        return "board-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute BoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("boardId", id);
            model.addAttribute("boardTypes", BoardType.values());
            return "board-edit";
        }
        boardService.update(id, boardRequest, principal.getUser());
        return "redirect:/boards/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserPrincipal principal) {
        boardService.delete(id, principal.getUser());
        return "redirect:/boards";
    }
}
