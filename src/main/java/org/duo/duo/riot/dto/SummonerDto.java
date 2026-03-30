package org.duo.duo.riot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummonerDto {
    private String id;
    private String accountId;
    private String puuid;
    private int profileIconId;
    private long summonerLevel;
}