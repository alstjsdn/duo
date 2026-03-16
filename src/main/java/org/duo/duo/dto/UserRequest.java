package org.duo.duo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{8,11}$",
             message = "아이디는 소문자 영어와 숫자만 가능하며, 8자 이상 11자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}