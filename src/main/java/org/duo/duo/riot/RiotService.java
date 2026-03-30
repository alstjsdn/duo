package org.duo.duo.riot;

import lombok.RequiredArgsConstructor;
import org.duo.duo.riot.dto.AccountDto;
import org.duo.duo.riot.dto.LeagueEntryDto;
import org.duo.duo.riot.dto.MatchDto;
import org.duo.duo.riot.dto.MatchHistoryDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotService {

    private final RiotClient riotClient;

    public AccountDto getAccount(String gameName, String tagLine) {
        return riotClient.getAccount(gameName, tagLine);
    }

    public List<MatchHistoryDto> getRecentMatches(String gameName, String tagLine, int count) {
        AccountDto account = riotClient.getAccount(gameName, tagLine);
        String puuid = account.getPuuid();

        return riotClient.getMatchIds(puuid, count).stream()
                .map(riotClient::getMatch)
                .map(match -> {
                    MatchDto.ParticipantDto myData = match.getInfo().getParticipants().stream()
                            .filter(p -> p.getPuuid().equals(puuid))
                            .findFirst().orElseThrow();

                    return MatchHistoryDto.builder()
                            .playedAt(Instant.ofEpochMilli(match.getInfo().getGameStartTimestamp())
                                    .atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                            .championName(myData.getChampionName())
                            .kills(myData.getKills())
                            .deaths(myData.getDeaths())
                            .assists(myData.getAssists())
                            .win(myData.isWin())
                            .gameMode(match.getInfo().getGameMode())
                            .itemImageUrls(MatchHistoryDto.toItemImageUrls(
                                    myData.getItem0(), myData.getItem1(), myData.getItem2(),
                                    myData.getItem3(), myData.getItem4(), myData.getItem5(),
                                    myData.getItem6()))
                            .build();
                })
                .toList();
    }

    public LeagueEntryDto getMyRank(String gameName, String tagLine) {

        AccountDto account = riotClient.getAccount(gameName, tagLine);
        String puuid = account.getPuuid();

        return riotClient.getLeagueEntries(puuid).stream()
                .filter(dto -> "RANKED_SOLO_5x5".equals(dto.getQueueType()))
                .findFirst().orElse(null);
    }

}
