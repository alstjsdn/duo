package org.duo.duo.board.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.security.UserPrincipal;
import org.duo.duo.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/write")
    public String create(@PathVariable Long boardId,
                         @RequestParam(required = false) Long parentId,
                         @Valid @ModelAttribute CommentRequest request,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", bindingResult.getFieldError("content").getDefaultMessage());
            return "redirect:/boards/" + boardId;
        }
        commentService.create(principal.getUser(), boardId, parentId, request);
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/{commentId}/edit")
    public String update(@Valid @ModelAttribute CommentRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal,
                         @PathVariable Long boardId,
                         @PathVariable Long commentId,
                         RedirectAttributes redirectAttributes,
                         BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", bindingResult.getFieldError("content").getDefaultMessage());
            return "redirect:/boards/" + boardId;
        }

        commentService.update(commentId, request,userPrincipal.getUser());
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/{commentId}/delete")
    public String delete(@AuthenticationPrincipal UserPrincipal userPrincipal,
                         @PathVariable Long boardId,
                         @PathVariable Long commentId,
                         RedirectAttributes redirectAttributes){

        commentService.delete(commentId, userPrincipal.getUser());
        return "redirect:/boards/" + boardId;
    }
}
