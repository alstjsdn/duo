package org.duo.duo.riot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {
    private String matchId;
    private MetadataDto metadata;
    private InfoDto info;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetadataDto {
        private String matchId;
        private List<String> participants;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoDto {
        private long gameDuration;
        private long gameStartTimestamp;
        private String gameMode;
        private String gameType;
        private List<ParticipantDto> participants;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParticipantDto {
        private String puuid;
        private String summonerName;
        private String championName;
        private int kills;
        private int deaths;
        private int assists;
        private boolean win;
        private int totalMinionsKilled;
        private int visionScore;
        private int item0;
        private int item1;
        private int item2;
        private int item3;
        private int item4;
        private int item5;
        private int item6;


    }
}