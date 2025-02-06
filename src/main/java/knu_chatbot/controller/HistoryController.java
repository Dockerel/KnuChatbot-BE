package knu_chatbot.controller;

import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.controller.response.HistoryResponse;
import knu_chatbot.service.HistoryService;
import knu_chatbot.util.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ApiResponse<List<HistoryResponse>> getAllHistories(@SessionAttribute(SessionConst.LOGIN_MEMBER) Long memberId) {
        List<HistoryResponse> histories = historyService.getAllHistoriesByMemberId(memberId).stream()
            .map(history -> HistoryResponse.of(history))
            .collect(Collectors.toList());
        return ApiResponse.ok(histories);
    }
}
