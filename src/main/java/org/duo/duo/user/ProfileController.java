package org.duo.duo.user;

import lombok.RequiredArgsConstructor;
import org.duo.duo.board.BoardService;
import org.duo.duo.board.comment.CommentService;
import org.duo.duo.common.security.UserPrincipal;
import org.duo.duo.common.service.ImageService;
import org.duo.duo.riot.RiotService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final RiotService riotService;
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = principal.getUser();
        model.addAttribute("user", user);
        model.addAttribute("boards", boardService.findMyboard(user));
        model.addAttribute("comments", commentService.findMyComment(user));
        if (user.getRiotId() != null && user.getRiotTag() != null) {
            model.addAttribute("rank", riotService.getMyRank(user.getRiotId(), user.getRiotTag()));
        }
        return "profile";
    }

    @PostMapping("/edit")
    public String editProfile(@AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam(required = false) String bio,
                              @RequestParam(required = false) MultipartFile profileImage) {
        userService.updateProfile(principal.getUser(), bio, profileImage);
        return "redirect:/profile";
    }
}