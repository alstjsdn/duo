package org.duo.duo.board.join;

import lombok.Getter;
import org.duo.duo.board.GameLine;

import java.time.LocalDateTime;

@Getter
public class JoinRequestResponse {

    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String riotId;
    private String riotTag;
    private GameLine gameLine;
    private JoinRequestStatus status;
    private LocalDateTime createdAt;

    public static JoinRequestResponse from(JoinRequest request) {
        JoinRequestResponse r = new JoinRequestResponse();
        r.id = request.getId();
        r.userId = request.getUser().getUserId();
        r.username = request.getUser().getUsername();
        r.name = request.getUser().getName();
        r.riotId = request.getUser().getRiotId();
        r.riotTag = request.getUser().getRiotTag();
        r.gameLine = request.getGameLine();
        r.status = request.getStatus();
        r.createdAt = request.getCreatedAt();
        return r;
    }
}