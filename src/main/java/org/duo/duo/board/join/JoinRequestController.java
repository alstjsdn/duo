package org.duo.duo.board.join;

import lombok.RequiredArgsConstructor;
import org.duo.duo.board.GameLine;
import org.duo.duo.common.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards/{boardId}/join")
@RequiredArgsConstructor
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @PostMapping
    public String requestJoin(@PathVariable Long boardId,
                              @RequestParam GameLine gameLine,
                              @AuthenticationPrincipal UserPrincipal principal) {
        joinRequestService.requestJoin(boardId, gameLine, principal.getUser());
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/{requestId}/approve")
    public String approve(@PathVariable Long boardId,
                          @PathVariable Long requestId,
                          @AuthenticationPrincipal UserPrincipal principal) {
        joinRequestService.approve(boardId, requestId, principal.getUser());
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/{requestId}/reject")
    public String reject(@PathVariable Long boardId,
                         @PathVariable Long requestId,
                         @AuthenticationPrincipal UserPrincipal principal) {
        joinRequestService.reject(boardId, requestId, principal.getUser());
        return "redirect:/boards/" + boardId;
    }

    @PostMapping("/{requestId}/kick")
    public String kick(@PathVariable Long boardId,
                       @PathVariable Long requestId,
                       @AuthenticationPrincipal UserPrincipal principal) {
        joinRequestService.kick(boardId, requestId, principal.getUser());
        return "redirect:/boards/" + boardId;
    }
}