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
import org.springframework.web.bind.annotation.*;
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
        UserResponse userResponse = UserResponse.from(user);
        model.addAttribute("user", userResponse);
        model.addAttribute("boards", boardService.findMyboard(user));
        model.addAttribute("comments", commentService.findMyComment(user));
        if (userResponse.getRiotId() != null && userResponse.getRiotTag() != null) {
            model.addAttribute("rank", riotService.getMyRank(userResponse.getRiotId(), userResponse.getRiotTag()));
        }
        return "profile";
    }

    @GetMapping("/{username}")
    public String publicProfile(@PathVariable String username,
                                @AuthenticationPrincipal UserPrincipal principal,
                                Model model) {
        UserResponse target = userService.getUserByUsername(username);

        // 본인이면 마이페이지로 리다이렉트
        if (principal != null && principal.getUser().getUsername().equals(username)) {
            return "redirect:/profile";
        }

        model.addAttribute("target", target);
        model.addAttribute("boards", boardService.findMyboard(userService.getUserEntity(username)));
        if (target.getRiotId() != null && target.getRiotTag() != null) {
            model.addAttribute("rank", riotService.getMyRank(target.getRiotId(), target.getRiotTag()));
        }
        return "public-profile";
    }

    @PostMapping("/edit")
    public String editProfile(@AuthenticationPrincipal UserPrincipal principal,
                              @RequestParam(required = false) String bio,
                              @RequestParam(required = false) MultipartFile profileImage) {
        userService.updateProfile(principal.getUser(), bio, profileImage);
        return "redirect:/profile";
    }

}