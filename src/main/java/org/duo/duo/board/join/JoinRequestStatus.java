package org.duo.duo.board.join;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JoinRequestStatus {
    PENDING("요청중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String name;
}