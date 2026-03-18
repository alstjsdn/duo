package org.duo.duo.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.duo.duo.user.User;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotNull(message = "게시판 유형을 선택해주세요.")
    private BoardType type;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .type(this.type)
                .title(this.title)
                .content(this.content)
                .viewCount(0)
                .build();
    }
}
