package org.duo.duo.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardStatus {

    RECRUITING("모집중"),
    COMPLETED("모집완료");

    private final String name;
}