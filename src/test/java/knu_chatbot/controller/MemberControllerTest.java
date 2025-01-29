package knu_chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.controller.request.MemberEmailCheckRequest;
import knu_chatbot.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("존재하지 않는 이메일은 상태코드 200을 받는다.")
    @Test
    void checkEmailWithNonExistentEmail() throws Exception {
        // given
        String email = "test@test.com";
        MemberEmailCheckRequest request = MemberEmailCheckRequest.builder()
            .email(email)
            .build();

        doNothing().when(memberService).emailExists(email);

        // when // then
        mockMvc.perform(
                post("/api/member/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("존재하는 이메일은 상태코드 400을 받는다.")
    @Test
    void checkEmailWithExistentEmail() throws Exception {
        // given
        String email = "test@test.com";
        MemberEmailCheckRequest request = MemberEmailCheckRequest.builder()
            .email(email)
            .build();

        doThrow(IllegalArgumentException.class).when(memberService).emailExists(email);

        // when // then
        mockMvc.perform(
                post("/api/member/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }



}