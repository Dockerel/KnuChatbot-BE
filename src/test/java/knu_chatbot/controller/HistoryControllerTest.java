package knu_chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.entity.History;
import knu_chatbot.service.HistoryService;
import knu_chatbot.util.SessionConst;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void getHistoriesByMemberId() throws Exception {
        // given
        History history = History.builder()
            .id(1L)
            .name("history1")
            .build();
        history.setCreatedAt(LocalDateTime.now());

        Long memberId = 1L;

        doReturn(List.of(history)).when(historyService).getAllHistoriesByMemberId(memberId);

        // when // then
        mockMvc.perform(
                get("/api/histories")
                    .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                    .content(objectMapper.writeValueAsString(memberId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data").isArray());
    }

}