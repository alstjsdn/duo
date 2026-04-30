package org.duo.duo.freeboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeBoardSearchRequest {
    private FreeBoardType type;
    private String title;
    private String content;
}