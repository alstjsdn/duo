package org.duo.duo.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.dto.ApiResponse;
import org.duo.duo.dto.UserRequest;
import org.duo.duo.dto.UserResponse;
import org.duo.duo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ApiResponse.success(response, "회원가입이 완료되었습니다.");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ApiResponse.success(response);
    }
}