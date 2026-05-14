package org.duo.duo.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.duo.duo.common.response.ApiResponse;
import org.duo.duo.common.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TrollReportController {

    private final TrollReportService trollReportService;

    @GetMapping("/api/reports/recent-members")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<RecentMemberDto>>> recentMembers(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<RecentMemberDto> members = trollReportService.getRecentPartyMembers(principal.getUser());
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @PostMapping("/api/reports")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> report(
            @Valid @RequestBody TrollReportRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            trollReportService.report(principal.getUser(), request);
            return ResponseEntity.ok(ApiResponse.success(null, "신고가 접수되었습니다."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/trolls")
    public String trollList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<TrollReportResponse> reports = trollReportService.getReports(
                PageRequest.of(page, 15, Sort.by("createdAt").descending()));
        model.addAttribute("reports", reports);
        return "troll-list";
    }
}