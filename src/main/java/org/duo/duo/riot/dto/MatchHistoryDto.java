package org.duo.duo.riot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class MatchHistoryDto {

    private static final String DDRAGON_BASE = "https://ddragon.leagueoflegends.com/cdn/15.6.1/img/";

    private LocalDateTime playedAt;
    private String championName;
    private int kills;
    private int deaths;
    private int assists;
    private boolean win;
    private String gameMode;
    private List<String> itemImageUrls;

    public String getChampionImageUrl() {
        return DDRAGON_BASE + "champion/" + championName + ".png";
    }

    public static List<String> toItemImageUrls(int... items) {
        return Arrays.stream(items)
                .filter(id -> id != 0)
                .mapToObj(id -> DDRAGON_BASE + "item/" + id + ".png")
                .toList();
    }
}