package org.duo.duo.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchRequest {

    private BoardType type;
    private String title;
    private String content;
    private GameLine neededLine;
}