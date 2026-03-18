package org.duo.duo.common.constants;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageConstants {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PAGE = "0";

    public static PageRequest of(int page, Sort sort) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);
    }

    private PageConstants() {}
}
