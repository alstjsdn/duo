package org.duo.duo.freeboard;

import org.springframework.data.jpa.domain.Specification;

public class FreeBoardSpecification {

    public static Specification<FreeBoard> hasType(FreeBoardType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<FreeBoard> titleContains(String title) {
        return (root, query, cb) ->
                (title == null || title.isBlank()) ? null : cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<FreeBoard> contentContains(String content) {
        return (root, query, cb) ->
                (content == null || content.isBlank()) ? null : cb.like(root.get("content"), "%" + content + "%");
    }
}