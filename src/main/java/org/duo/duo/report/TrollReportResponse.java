package org.duo.duo.report;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TrollReportResponse {

    private final Long id;
    private final Long reporterUserId;
    private final String reporterName;
    private final Long reportedUserId;
    private final String reportedName;
    private final String reportedRiotId;
    private final String reportedRiotTag;
    private final Long boardId;
    private final String boardTitle;
    private final String content;
    private final LocalDateTime createdAt;

    private TrollReportResponse(TrollReport r) {
        this.id = r.getId();
        this.reporterUserId = r.getReporter().getUserId();
        this.reporterName = r.getReporter().getName();
        this.reportedUserId = r.getReported().getUserId();
        this.reportedName = r.getReported().getName();
        this.reportedRiotId = r.getReported().getRiotId();
        this.reportedRiotTag = r.getReported().getRiotTag();
        this.boardId = r.getBoard() != null ? r.getBoard().getBoardId() : null;
        this.boardTitle = r.getBoard() != null ? r.getBoard().getTitle() : "(삭제된 파티)";
        this.content = r.getContent();
        this.createdAt = r.getCreatedAt();
    }

    public static TrollReportResponse from(TrollReport r) {
        return new TrollReportResponse(r);
    }
}