package org.duo.duo.riot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    private String puuid;
    private String gameName;
    private String tagLine;
}