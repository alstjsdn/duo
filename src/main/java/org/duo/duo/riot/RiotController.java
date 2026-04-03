package org.duo.duo.riot;

import lombok.RequiredArgsConstructor;
import org.duo.duo.riot.dto.MatchHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/riot")
@RequiredArgsConstructor
public class RiotController {

    private final RiotService riotService;

    @GetMapping("/matches")
    public ResponseEntity<List<MatchHistoryDto>> getMatches(@RequestParam String riotId,
                                                            @RequestParam String riotTag) {
        return ResponseEntity.ok(riotService.getRecentMatches(riotId, riotTag, 10));
    }
}