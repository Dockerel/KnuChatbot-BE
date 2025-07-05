package knu_chatbot.controller;

import jakarta.validation.Valid;
import knu_chatbot.controller.request.UpdateHistoryNameRequest;
import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.controller.response.CreateNewChatResponse;
import knu_chatbot.controller.response.HistoryResponse;
import knu_chatbot.controller.response.QuestionAndAnswerResponse;
import knu_chatbot.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static knu_chatbot.util.SessionConst.LOGIN_MEMBER;

@RestController
@RequestMapping("/api/v1/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ApiResponse<List<HistoryResponse>> getAllHistories(@SessionAttribute(LOGIN_MEMBER) Long memberId) {
        return ApiResponse.ok(historyService.getAllHistoriesByMemberId(memberId));
    }

    @PostMapping
    public ApiResponse<HistoryResponse> createHistory(@SessionAttribute(LOGIN_MEMBER) Long memberId) {
        return ApiResponse.ok(historyService.createHistory(memberId));
    }

    @GetMapping("/{historyId}")
    public ApiResponse<List<QuestionAndAnswerResponse>> getAllQuestionsAndAnswersByHistoryId(
            @SessionAttribute(LOGIN_MEMBER) Long memberId,
            @PathVariable("historyId") Long historyId
    ) {
        return ApiResponse.ok(historyService.getAllQuestionsAndAnswers(memberId, historyId));
    }

    @PostMapping("/{historyId}")
    public ApiResponse<CreateNewChatResponse> createNewChatByHistoryId(
            @SessionAttribute(LOGIN_MEMBER) Long memberId,
            @PathVariable("historyId") Long historyId,
            @Valid @RequestBody CreateNewChatRequest request
    ) {
        return ApiResponse.ok(historyService.createNewChat(memberId, historyId, request));
    }

    @PutMapping("/{historyId}")
    public ApiResponse<HistoryResponse> updateHistoryName(
            @SessionAttribute(LOGIN_MEMBER) Long memberId,
            @PathVariable("historyId") Long historyId,
            @Valid @RequestBody UpdateHistoryNameRequest request
    ) {
        return ApiResponse.ok(historyService.updateHistory(memberId, historyId, request.toServiceRequest()));
    }

    @DeleteMapping("/{historyId}")
    public ApiResponse<String> deleteHistory(
            @SessionAttribute(LOGIN_MEMBER) Long memberId,
            @PathVariable("historyId") Long historyId
    ) {
        return ApiResponse.ok(historyService.deleteHistory(memberId, historyId));
    }
}
