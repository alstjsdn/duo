package org.duo.duo.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    QUESTION("질문"),
    FREE("자유"),
    RANK("랭크"),
    CLASSIC("일반");

    private final String name;
}
