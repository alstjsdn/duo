package org.duo.duo.freeboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.constants.PageConstants;
import org.duo.duo.common.security.UserPrincipal;
import org.duo.duo.freeboard.comment.FreeBoardCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/freeboards")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;
    private final FreeBoardCommentService freeBoardCommentService;

    @GetMapping
    public String list(@ModelAttribute FreeBoardSearchRequest request,
                       @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int page,
                       Model model) {
        PageRequest pageable = PageConstants.of(page, Sort.by("createdAt").descending());
        Page<FreeBoardResponse> result = freeBoardService.search(request, pageable);
        model.addAttribute("boards", result);
        model.addAttribute("search", request);
        model.addAttribute("boardTypes", FreeBoardType.values());
        return "freeboard-list";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("boardRequest", new FreeBoardRequest());
        model.addAttribute("boardTypes", FreeBoardType.values());
        return "freeboard-write";
    }

    @PostMapping("/write")
    public String create(@Valid @ModelAttribute FreeBoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardTypes", FreeBoardType.values());
            return "freeboard-write";
        }
        freeBoardService.create(boardRequest, principal.getUser());
        return "redirect:/freeboards";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int commentPage,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        FreeBoardResponse board = freeBoardService.view(id);
        PageRequest pageable = PageConstants.of(commentPage, Sort.by("createdAt").ascending());
        model.addAttribute("board", board);
        model.addAttribute("comments", freeBoardCommentService.findByBoardId(id, pageable));
        return "freeboard-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("boardId", id);
        model.addAttribute("boardRequest", FreeBoardRequest.from(freeBoardService.get(id)));
        model.addAttribute("boardTypes", FreeBoardType.values());
        return "freeboard-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute FreeBoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardId", id);
            model.addAttribute("boardTypes", FreeBoardType.values());
            return "freeboard-edit";
        }
        freeBoardService.update(id, boardRequest, principal.getUser());
        return "redirect:/freeboards/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserPrincipal principal) {
        freeBoardService.delete(id, principal.getUser());
        return "redirect:/freeboards";
    }
}