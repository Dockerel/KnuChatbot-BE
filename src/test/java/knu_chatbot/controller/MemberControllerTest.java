package knu_chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.controller.request.MemberEmailCheckRequest;
import knu_chatbot.controller.request.MemberLoginRequest;
import knu_chatbot.controller.request.MemberSignupRequest;
import knu_chatbot.service.MemberService;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.util.SessionConst;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        doNothing().when(memberService).emailExists(request.toServiceRequest());

        // when // then
        mockMvc.perform(
                post("/api/members/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("존재하는 이메일은 상태코드 400을 받는다.")
    @Test
    void checkEmailWithExistentEmail() throws Exception {
        // given
        String email = "test@test.com";
        MemberEmailCheckRequest request = MemberEmailCheckRequest.builder()
            .email(email)
            .build();

        doThrow(IllegalArgumentException.class).when(memberService).emailExists(any(MemberEmailCheckServiceRequest.class));

        // when // then
        mockMvc.perform(
                post("/api/members/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @DisplayName("이메일 확인시 이메일은 필수값이다.")
    @Test
    void checkEmailWithoutEmail() throws Exception {
        // given
        MemberEmailCheckRequest request = MemberEmailCheckRequest.builder().build();

        // when // then
        mockMvc.perform(
                post("/api/members/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 이메일은 필수입니다."));
    }

    @DisplayName("이메일 확인 시 이메일은 이메일 형태를 가져야 한다.")
    @Test
    void emailCheckEmailFormatTest() throws Exception {
        // given
        MemberEmailCheckRequest request = MemberEmailCheckRequest.builder()
            .email("test.test.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/check-email")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일 형태로 입력해주세요."));
    }

    @DisplayName("이메일, 비밀번호, 닉네임으로 회원가입한다.")
    @Test
    void signupTest() throws Exception {
        // given
        MemberSignupRequest request = MemberSignupRequest.builder()
            .email("test@test.com")
            .password("testPW")
            .nickname("testNickname")
            .build();

        // when
        doNothing().when(memberService).signup(request.toServiceRequest());

        // then
        mockMvc.perform(
                post("/api/members/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("회원가입시 이메일은 필수값이다.")
    @Test
    void signupWithoutEmail() throws Exception {
        // given
        MemberSignupRequest request = MemberSignupRequest.builder()
            .password("testPW")
            .nickname("testNickname")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 이메일은 필수입니다."));
    }

    @DisplayName("회원가입시 비밀번호는 필수값이다.")
    @Test
    void signupWithoutPassword() throws Exception {
        // given
        MemberSignupRequest request = MemberSignupRequest.builder()
            .email("test@test.com")
            .nickname("testNickname")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 비밀번호는 필수입니다."));
    }

    @DisplayName("회원가입시 닉네임은 필수값이다.")
    @Test
    void signupWithoutNickname() throws Exception {
        // given
        MemberSignupRequest request = MemberSignupRequest.builder()
            .email("test@test.com")
            .password("testPW")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 닉네임은 필수입니다."));
    }

    @DisplayName("이메일과 비밀번호로 로그인한다.")
    @Test
    void loginTest() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("test@test.com")
            .password("testPW")
            .build();

        // when // then
        MvcResult result = mockMvc.perform(
                post("/api/members/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String setCookieHeader = result.getResponse().getHeader("Set-Cookie");

        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("JSESSIONID");
    }

    @DisplayName("로그인시 이메일은 필수값이다.")
    @Test
    void loginWithoutEmail() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .password("testPW")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 이메일은 필수입니다."));
    }

    @DisplayName("로그인시 비밀번호는 필수값이다.")
    @Test
    void loginWithoutPassword() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("test@test.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/members/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("유저 비밀번호는 필수입니다."));
    }

    @DisplayName("로그인시 이메일 혹은 비밀번호가 틀리면 상태코드 400을 받는다.")
    @Test
    void loginWithWrongEmailOrPassword() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("test@test.com")
            .password("testPW")
            .build();

        doThrow(IllegalArgumentException.class).when(memberService).login(any(MemberLoginServiceRequest.class));

        // when // then
        mockMvc.perform(
                post("/api/members/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @DisplayName("")
    @Test
    void getMyInfoWithoutNonExistMemberId() throws Exception {
        // given
        Long memberId = 1L;

        doThrow(IllegalArgumentException.class).when(memberService).getMyInfo(any(Long.class));

        // when // then
        mockMvc.perform(
                get("/api/members/me")
                    .sessionAttr(SessionConst.LOGIN_MEMBER, memberId)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

}