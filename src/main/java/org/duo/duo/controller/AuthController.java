package org.duo.duo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.dto.UserRequest;
import org.duo.duo.exception.CustomException;
import org.duo.duo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "signup";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserRequest userRequest,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.createUser(userRequest);
            return "redirect:/login?signup=success";
        } catch (CustomException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
}