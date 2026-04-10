package org.duo.duo.board;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BoardSpecification {

    public static Specification<Board> hasType(BoardType type) {
        return (root, query, cb) ->
                type != null ? cb.equal(root.get("type"), type) : null;
    }

    public static Specification<Board> titleContains(String title) {
        return (root, query, cb) ->
                StringUtils.hasText(title) ? cb.like(root.get("title"), "%" + title + "%") : null;
    }

    public static Specification<Board> contentContains(String content) {
        return (root, query, cb) ->
                StringUtils.hasText(content) ? cb.like(root.get("content"), "%" + content + "%") : null;
    }

    public static Specification<Board> neededLineContains(GameLine neededLine) {
        return (root, query, cb) -> {
            if (neededLine == null) return null;
            query.distinct(true);
            var join = root.join("neededLines", JoinType.INNER);
            return cb.equal(join, neededLine);
        };
    }
}