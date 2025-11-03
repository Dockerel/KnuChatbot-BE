package knu_chatbot.presentation.controller;

import jakarta.servlet.http.Cookie;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.dto.request.ChangePasswordServiceRequest;
import knu_chatbot.application.dto.request.LoginServiceRequest;
import knu_chatbot.application.dto.request.SignupServiceRequest;
import knu_chatbot.application.dto.response.*;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.presentation.request.ChangePasswordRequest;
import knu_chatbot.presentation.request.CheckEmailRequest;
import knu_chatbot.presentation.request.LoginRequest;
import knu_chatbot.presentation.request.SignupRequest;
import knu_chatbot.presentation.response.MyPageResponse;
import knu_chatbot.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static knu_chatbot.application.util.AuthConst.REFRESH_TOKEN;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends ControllerTestSupport {

    @DisplayName("이메일 중복을 체크한다.")
    @Test
    void checkEmail() throws Exception {
        // given
        CheckEmailRequest request = CheckEmailRequest.builder()
                .email("test@test.com")
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/members/check-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"));
    }

    @DisplayName("이메일 중복을 체크 시 이메일은 필수 값이다.")
    @Test
    void checkEmailWithoutEmail() throws Exception {
        // given
        CheckEmailRequest request = CheckEmailRequest.builder()
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/members/check-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이메일은 필수입니다."));
    }

    @DisplayName("이메일 중복을 체크 시 이메일은 이메일 형태를 가져야 한다.")
    @Test
    void checkEmailWithoutEmailFormat() throws Exception {
        // given
        CheckEmailRequest request = CheckEmailRequest.builder()
                .email("test")
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/members/check-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이메일 형태가 아닙니다."));
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void signup() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("password")
                .confirmPassword("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"));
    }

    @DisplayName("회원가입 시 이메일은 필수 값이다.")
    @Test
    void signupWithoutEmail() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .password("password")
                .confirmPassword("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이메일은 필수입니다."));
    }

    @DisplayName("회원가입 시 이메일은 이메일 형태를 가져야 한다.")
    @Test
    void signupWithoutEmailFormat() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test")
                .password("password")
                .confirmPassword("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이메일 형태가 아닙니다."));
    }

    @DisplayName("회원가입 시 비밀번호는 필수 값이다.")
    @Test
    void signupWithoutPassword() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .confirmPassword("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("비밀번호는 필수입니다."));
    }

    @DisplayName("회원가입 시 비밀번호의 길이는 최소 8글자 입니다.")
    @Test
    void signupWithShortPassword() throws Exception {
        // given
        String password = "a".repeat(7);
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password(password)
                .confirmPassword(password)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("길이는 최소 8글자, 최대 20글자 입니다."));
    }

    @DisplayName("회원가입 시 비밀번호의 길이는 최대 20글자 입니다.")
    @Test
    void signupWithLongPassword() throws Exception {
        // given
        String password = "a".repeat(21);
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password(password)
                .confirmPassword(password)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("길이는 최소 8글자, 최대 20글자 입니다."));
    }

    @DisplayName("회원가입 시 비밀번호 확인은 필수 값이다.")
    @Test
    void signupWithoutPasswordConfirm() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("비밀번호 확인은 필수입니다."));
    }

    @DisplayName("회원가입 시 비밀번호 확인이 비밀번호와 일치해야 한다.")
    @Test
    void signupWithWrongPasswordConfirm() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("password")
                .confirmPassword("password")
                .build();

        given(memberService.signup(any(SignupServiceRequest.class)))
                .willThrow(new KnuChatbotException(ErrorType.USER_CONFIRM_PASSWORD_ERROR, request));

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("비밀번호 확인이 일치하지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"));
    }

    @DisplayName("이미 존재하는 이메일로는 회원가입이 불가능하다.")
    @Test
    void signupWithExistentEmail() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@test.com")
                .password("password")
                .confirmPassword("password")
                .build();

        given(memberService.signup(any(SignupServiceRequest.class)))
                .willThrow(new KnuChatbotException(ErrorType.USER_INVALID_EMAIL_ERROR, request.getEmail()));

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("이미 존재하는 이메일입니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("CONFLICT"));
    }

    @DisplayName("로그인을 한다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("password")
                .build();

        LoginResponse response = LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        given(memberService.login(any(LoginServiceRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
                .andExpect(header().string("Set-Cookie", containsString("refreshToken")))
                .andExpect(header().string("Set-Cookie", containsString("Path")))
                .andExpect(header().string("Set-Cookie", containsString("Max-Age")))
                .andExpect(header().string("Set-Cookie", containsString("Secure")))
                .andExpect(header().string("Set-Cookie", containsString("SameSite=None")));
    }

    @DisplayName("로그인 시 이메일은 필수 값이다.")
    @Test
    void loginWithoutEmail() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .password("password")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이메일은 필수입니다."));
    }

    @DisplayName("로그인 시 비밀번호는 필수 값이다.")
    @Test
    void loginWithoutPassword() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("비밀번호는 필수입니다."));
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void reissueTokens() throws Exception {
        // given
        String refreshToken = "reissueTokensRefreshToken";
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);

        ReissueTokensResponse response = ReissueTokensResponse.of("NewAccessToken", "NewRefreshToken");

        given(memberService.reissueTokens(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/reissue")
                                .cookie(cookie)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"));
    }

    @DisplayName("토큰을 재발급 시 쿠키에 RefreshToken이 존재해야 한다.")
    @Test
    void reissueTokensWithoutRefreshToken() throws Exception {
        // given // when // then
        mockMvc.perform(
                        post("/api/v1/members/reissue")
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("쿠키에 RefreshToken이 없습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("UNAUTHORIZED"));
    }

    @DisplayName("마이페이지 정보를 불러온다.")
    @Test
    void getMyPage() throws Exception {
        // given
        MyPageResponse response = MyPageResponse.builder()
                .email(TEST_EMAIL)
                .build();

        given(memberService.getMyPage(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/members/me")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
                .andExpect(jsonPath("$.data.email").value(TEST_EMAIL));
    }

    @DisplayName("로그아웃 한다.")
    @Test
    void logout() throws Exception {
        // given
        String message = "로그아웃이 완료되었습니다.";
        LogoutResponse response = LogoutResponse.of(message);

        given(memberService.logout(anyString()))
                .willReturn(response);

        String refreshToken = "reissueTokensRefreshToken";
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);

        // when // then
        mockMvc.perform(
                        post("/api/v1/members/logout")
                                .cookie(cookie)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
                .andExpect(jsonPath("$.data.message").value(message));
    }

    @DisplayName("회원탈퇴 한다.")
    @Test
    void withdraw() throws Exception {
        // given
        String message = "회원탈퇴 완료되었습니다.";
        WithdrawResponse response = WithdrawResponse.of(message);

        given(memberService.withdraw(anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        delete("/api/v1/members")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
                .andExpect(jsonPath("$.data.message").value(message));
    }

    @DisplayName("비밀번호를 변경한다.")
    @Test
    void changePassword() throws Exception {
        // given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        String message = "비밀번호 변경이 완료되었습니다.";
        ChangePasswordResponse response = ChangePasswordResponse.of(message);

        given(memberService.changePassword(any(AuthUser.class), any(ChangePasswordServiceRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        patch("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultType").value("SUCCESS"))
                .andExpect(jsonPath("$.data.message").value(message));
    }

    @DisplayName("비밀번호를 변경 시 이전 비밀번호는 필수값이다.")
    @Test
    void changePasswordWithoutOldPassword() throws Exception {
        // given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .newPassword("newPassword")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("이전 비밀번호는 필수입니다."));
    }

    @DisplayName("비밀번호를 변경 시 새로운 비밀번호는 필수값이다.")
    @Test
    void changePasswordWithoutNewPassword() throws Exception {
        // given
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("oldPassword")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("새로운 비밀번호는 필수입니다."));
    }

    @DisplayName("비밀번호를 변경 시 비밀번호의 길이는 최소 8글자 입니다.")
    @Test
    void changePasswordWithShortNewPassword() throws Exception {
        // given
        String newPassword = "a".repeat(7);

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("oldPassword")
                .newPassword(newPassword)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("길이는 최소 8글자, 최대 20글자 입니다."));
    }

    @DisplayName("비밀번호를 변경 시 비밀번호의 길이는 최대 20글자 입니다.")
    @Test
    void changePasswordWithLongNewPassword() throws Exception {
        // given
        String newPassword = "a".repeat(21);

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("oldPassword")
                .newPassword(newPassword)
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultType").value("ERROR"))
                .andExpect(jsonPath("$.errorMessage.message").value("요청이 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorMessage.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage.data").value("길이는 최소 8글자, 최대 20글자 입니다."));
    }

}