package org.duo.duo.freeboard.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/freeboards/{boardId}/comments")
@RequiredArgsConstructor
public class FreeBoardCommentController {

    private final FreeBoardCommentService commentService;

    @PostMapping("/write")
    public String create(@PathVariable Long boardId,
                         @RequestParam(required = false) Long parentId,
                         @Valid @ModelAttribute FreeBoardCommentRequest request,
                         @AuthenticationPrincipal UserPrincipal principal,
                         RedirectAttributes redirectAttributes) {
        try {
            commentService.create(principal.getUser(), boardId, parentId, request);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("commentError", e.getMessage());
        }
        return "redirect:/freeboards/" + boardId;
    }

    @PostMapping("/{commentId}/edit")
    public String update(@PathVariable Long boardId,
                         @PathVariable Long commentId,
                         @Valid @ModelAttribute FreeBoardCommentRequest request,
                         @AuthenticationPrincipal UserPrincipal principal) {
        commentService.update(commentId, request, principal.getUser());
        return "redirect:/freeboards/" + boardId;
    }

    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable Long boardId,
                         @PathVariable Long commentId,
                         @AuthenticationPrincipal UserPrincipal principal) {
        commentService.delete(commentId, principal.getUser());
        return "redirect:/freeboards/" + boardId;
    }
}