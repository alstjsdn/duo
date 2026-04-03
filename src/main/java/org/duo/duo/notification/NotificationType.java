package org.duo.duo.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    COMMENT("댓글"),
    REPLY("답글");

    private final String name;
}