package org.duo.duo.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.board.comment.CommentService;
import org.duo.duo.board.join.JoinRequestService;
import org.duo.duo.chat.ChatService;
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
    private final JoinRequestService joinRequestService;
    private final ChatService chatService;

    @GetMapping
    public String list(@ModelAttribute BoardSearchRequest request,
                       @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int page,
                       Model model) {
        PageRequest pageable = PageConstants.of(page, Sort.by("createdAt").descending());
        Page<BoardResponse> result = boardService.search(request, pageable);

        model.addAttribute("boards", result);
        model.addAttribute("search", request);
        model.addAttribute("boardTypes", BoardType.values());
        model.addAttribute("gameLines", GameLine.values());
        return "board-list";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("boardRequest", new BoardRequest());
        model.addAttribute("boardTypes", BoardType.values());
        model.addAttribute("gameLines", GameLine.values());
        return "board-write";
    }

    @PostMapping("/write")
    public String create(@Valid @ModelAttribute BoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardTypes", BoardType.values());
            model.addAttribute("gameLines", GameLine.values());
            return "board-write";
        }
        boardService.create(boardRequest, principal.getUser());
        return "redirect:/boards";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE) int commentPage,
                         @RequestParam(defaultValue = "false") boolean editBlocked,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        BoardResponse board = boardService.view(id);
        PageRequest pageable = PageConstants.of(commentPage, Sort.by("createdAt").ascending());
        boolean hasApproved = joinRequestService.hasApprovedRequests(id);
        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.findByBoardId(id, pageable));
        model.addAttribute("approvedByLine", joinRequestService.getApprovedByLine(id));
        model.addAttribute("pendingRequests", joinRequestService.getPendingRequests(id));
        model.addAttribute("hasApproved", hasApproved);
        model.addAttribute("editBlocked", editBlocked);
        if (principal != null) {
            Long userId = principal.getUser().getUserId();
            model.addAttribute("myPendingLines", joinRequestService.getMyPendingLines(id, userId));
            model.addAttribute("hasChatAccess", chatService.hasAccess(id, userId));
        }
        return "board-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        if (joinRequestService.hasApprovedRequests(id)) {
            return "redirect:/boards/" + id + "?editBlocked=true";
        }
        model.addAttribute("boardId", id);
        model.addAttribute("boardRequest", BoardRequest.from(boardService.get(id)));
        model.addAttribute("boardTypes", BoardType.values());
        model.addAttribute("gameLines", GameLine.values());
        return "board-edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute BoardRequest boardRequest,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (joinRequestService.hasApprovedRequests(id)) {
            return "redirect:/boards/" + id + "?editBlocked=true";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("boardId", id);
            model.addAttribute("boardTypes", BoardType.values());
            model.addAttribute("gameLines", GameLine.values());
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
