package org.duo.duo.riot;

import lombok.extern.slf4j.Slf4j;
import org.duo.duo.riot.dto.AccountDto;
import org.duo.duo.riot.dto.LeagueEntryDto;
import org.duo.duo.riot.dto.MatchDto;
import org.duo.duo.riot.dto.SummonerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
public class RiotClient {

    private static final String KR_BASE_URL = "https://kr.api.riotgames.com";
    private static final String ASIA_BASE_URL = "https://asia.api.riotgames.com";

    private final RestClient krClient;
    private final RestClient asiaClient;

    public RiotClient(@Value("${riot.api-key}") String apiKey) {
        this.krClient = RestClient.builder()
                .baseUrl(KR_BASE_URL)
                .defaultHeader("X-Riot-Token", apiKey)
                .build();
        this.asiaClient = RestClient.builder()
                .baseUrl(ASIA_BASE_URL)
                .defaultHeader("X-Riot-Token", apiKey)
                .build();
    }

    /** gameName + tagLine으로 계정 정보(puuid) 조회 */
    public AccountDto getAccount(String gameName, String tagLine) {
        return asiaClient.get()
                .uri("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}", gameName, tagLine)
                .retrieve()
                .body(AccountDto.class);
    }

    /** puuid로 소환사 정보 조회 (레벨, 아이콘 등) */
    public SummonerDto getSummonerByPuuid(String puuid) {
        return krClient.get()
                .uri("/lol/summoner/v4/summoners/by-puuid/{puuid}", puuid)
                .retrieve()
                .body(SummonerDto.class);
    }

    /** puuid로 최근 매치 ID 목록 조회 */
    public List<String> getMatchIds(String puuid, int count) {
        return asiaClient.get()
                .uri(uri -> uri
                        .path("/lol/match/v5/matches/by-puuid/{puuid}/ids")
                        .queryParam("count", count)
                        .build(puuid))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /** puuid로 리그 엔트리(랭크 정보) 조회 */
    public List<LeagueEntryDto> getLeagueEntries(String puuid) {
        return krClient.get()
                .uri("/lol/league/v4/entries/by-puuid/{puuid}", puuid)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /** matchId로 매치 상세 정보 조회 */
    public MatchDto getMatch(String matchId) {
        return asiaClient.get()
                .uri("/lol/match/v5/matches/{matchId}", matchId)
                .retrieve()
                .body(MatchDto.class);
    }
}