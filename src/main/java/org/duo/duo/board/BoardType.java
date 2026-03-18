package org.duo.duo.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    LEAGUE_OF_LEGEND("리그오브레전드"),
    VALORANT("발로란트"),
    OTHER("기타");

    private final String name;
}
