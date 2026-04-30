package org.duo.duo.freeboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FreeBoardType {
    FREE("자유"),
    QUESTION("질문"),
    HUMOR("유머");

    private final String name;
}