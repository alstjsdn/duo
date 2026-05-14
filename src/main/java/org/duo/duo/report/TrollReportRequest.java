package org.duo.duo.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrollReportRequest {

    @NotBlank(message = "라이엇 ID를 입력해주세요.")
    private String riotId;

    @NotBlank(message = "라이엇 태그를 입력해주세요.")
    private String riotTag;

    @NotBlank(message = "신고 내용을 입력해주세요.")
    private String content;
}