package knu_chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.controller.request.UpdateHistoryNameRequest;
import knu_chatbot.controller.response.AnswerResponse;
import knu_chatbot.controller.response.HistoryResponse;
import knu_chatbot.controller.response.QuestionAndAnswerResponse;
import knu_chatbot.controller.response.QuestionResponse;
import knu_chatbot.entity.History;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.service.HistoryService;
import knu_chatbot.service.request.UpdateHistoryNameServiceRequest;
import knu_chatbot.util.SessionConst;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HistoryService historyService;

    @DisplayName("memberId로 멤버의 히스토리를 불러온다.")
    @Test
    void getAllHistoriesByMemberIdTest() throws Exception {
        // given
        History history = History.builder()
                .id(1L)
                .name("history1")
                .build();

        Long memberId = 1L;

        HistoryResponse historyResponse = HistoryResponse.of(history);

        doReturn(List.of(historyResponse)).when(historyService).getAllHistoriesByMemberId(memberId);

        // when // then
        mockMvc.perform(
                        get("/api/v1/histories")
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("memberId로 새로운 히스토리를 생성한다.")
    @Test
    void createHistoryByMemberIdTest() throws Exception {
        // given
        History history = History.builder()
                .id(1L)
                .name("history1")
                .build();

        Long memberId = 1L;

        HistoryResponse historyResponse = HistoryResponse.of(history);

        doReturn(historyResponse).when(historyService).createHistory(memberId);

        // when // then
        mockMvc.perform(
                        post("/api/v1/histories")
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("memberId와 historyId로 히스토리의 질문들과 답변들을 가져온다.")
    @Test
    void getAllQuestionsAndAnswersByHistoryIdTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        QuestionAndAnswerResponse questionAndAnswerResponse = QuestionAndAnswerResponse.of(
                QuestionResponse.of("text"),
                AnswerResponse.of("text", "reference", List.of("image"))
        );

        doReturn(List.of(questionAndAnswerResponse)).when(historyService).getAllQuestionsAndAnswers(memberId, historyId);

        // when // then
        mockMvc.perform(
                        get("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("유효하지 않은 memberId로 요청 시 상태코드 400이 반환된다.")
    @Test
    void getAllQuestionsAndAnswersByInvalidMemberIdTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        doThrow(new KnuChatbotException("test", FORBIDDEN)).when(historyService).getAllQuestionsAndAnswers(memberId, historyId);

        // when // then
        mockMvc.perform(
                        get("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"));
    }

    @DisplayName("memberId와 historyId로 히스토리 이름을 업데이트한다.")
    @Test
    void updateHistoryNameTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        UpdateHistoryNameRequest request = UpdateHistoryNameRequest.of("test");
        HistoryResponse historyResponse = HistoryResponse.of(History.of("test"));

        doReturn(historyResponse).when(historyService).updateHistory(memberId, historyId, UpdateHistoryNameServiceRequest.of("test"));

        // when // then
        mockMvc.perform(
                        put("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("유효하지 않은 memberId로 요청 시 상태코드 400이 반환된다.")
    @Test
    void updateHistoryNameByInvalidMemberIdTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        UpdateHistoryNameRequest request = UpdateHistoryNameRequest.of("test");

        doThrow(new KnuChatbotException("test", FORBIDDEN)).when(historyService).updateHistory(eq(memberId), eq(historyId), any(UpdateHistoryNameServiceRequest.class));

        // when // then
        mockMvc.perform(
                        put("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"));
    }

    @DisplayName("memberId와 historyId로 히스토리를 삭제한다.")
    @Test
    void deleteHistoryTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        doReturn("test").when(historyService).deleteHistory(memberId, historyId);

        // when // then
        mockMvc.perform(
                        delete("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("memberId와 historyId로 히스토리를 삭제한다.")
    @Test
    void deleteHistoryByInvalidMemberIdTest() throws Exception {
        // given
        Long memberId = 1L;
        Long historyId = 1L;

        doThrow(new KnuChatbotException("test", FORBIDDEN)).when(historyService).deleteHistory(memberId, historyId);

        // when // then
        mockMvc.perform(
                        delete("/api/v1/histories/" + historyId)
                                .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"));
    }

}