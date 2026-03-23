package org.duo.duo.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequest {

    @NotNull(message = "게시판 유형을 선택해주세요.")
    private BoardType type;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public static BoardRequest from(BoardResponse board) {
        BoardRequest request = new BoardRequest();
        request.type = board.getType();
        request.title = board.getTitle();
        request.content = board.getContent();
        return request;
    }
}
