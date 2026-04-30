package org.duo.duo.freeboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FreeBoardRequest {

    @NotNull(message = "유형을 선택해주세요.")
    private FreeBoardType type;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public static FreeBoardRequest from(FreeBoardResponse response) {
        FreeBoardRequest request = new FreeBoardRequest();
        request.type = response.getType();
        request.title = response.getTitle();
        request.content = response.getContent();
        return request;
    }
}