package knu_chatbot.application.service;

import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.dto.request.CheckEmailServiceRequest;
import knu_chatbot.application.dto.request.LoginServiceRequest;
import knu_chatbot.application.dto.request.SignupServiceRequest;
import knu_chatbot.application.dto.response.*;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.util.JwtProvider;
import knu_chatbot.infrastructure.entity.Member;
import knu_chatbot.infrastructure.repository.MemberJpaRepository;
import knu_chatbot.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("회원이 존재하면 이메일로 존재 여부 확인 시 valid:false를 반환한다.")
    @Test
    void checkEmailWithExistEmail() {
        // given
        String existEmail = "email";

        CheckEmailServiceRequest request = CheckEmailServiceRequest.builder()
                .email(existEmail)
                .build();

        Member member = Member.builder()
                .email(existEmail)
                .password("password")
                .build();
        memberJpaRepository.save(member);

        // when
        CheckEmailResponse response = memberService.checkEmail(request);

        // then
        assertThat(response)
                .extracting("valid")
                .isEqualTo(false);
    }

    @DisplayName("회원이 존재하지 않으면 이메일로 존재 여부 확인 시 valid:true를 반환한다.")
    @Test
    void checkEmailWithNonExistentEmail() {
        // given
        String nonExistentEmail = "email";

        CheckEmailServiceRequest request = CheckEmailServiceRequest.builder()
                .email(nonExistentEmail)
                .build();

        // when
        CheckEmailResponse response = memberService.checkEmail(request);

        // then
        assertThat(response)
                .extracting("valid")
                .isEqualTo(true);
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void signup() {
        // given
        String password = "password";
        SignupServiceRequest request = SignupServiceRequest.builder()
                .email("test@test.com")
                .password(password)
                .confirmPassword(password)
                .build();

        // when
        SignupResponse response = memberService.signup(request);

        // then
        assertThat(response)
                .extracting("message")
                .isEqualTo("회원가입이 완료되었습니다.");
    }

    @DisplayName("회원가입 시 비밀번호 확인이 비밀번호와 일치해야 한다.")
    @Test
    void signupWithWrongPasswordConfirm() {
        // given
        String password = "password";
        String passwordConfirm = "wrongPasswordConfirm";

        SignupServiceRequest request = SignupServiceRequest.builder()
                .email("test@test.com")
                .password(password)
                .confirmPassword(passwordConfirm)
                .build();

        // when // then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_CONFIRM_PASSWORD_ERROR);
    }

    @DisplayName("로그인을 하면 accessToken과 refreshToken이 발급된다.")
    @Test
    void login() {
        // given
        String email = "test@test.com";
        String password = "password";

        SignupServiceRequest signupRequest = SignupServiceRequest.builder()
                .email(email)
                .password(password)
                .confirmPassword(password)
                .build();

        memberService.signup(signupRequest);

        LoginServiceRequest loginRequest = LoginServiceRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when
        LoginResponse response = memberService.login(loginRequest);

        // then
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
    }

    @DisplayName("로그인 시 올바른 비밀번호를 입력해야 한다.")
    @Test
    void loginWithWrongPassword() {
        // given
        String email = "test@test.com";
        String password = "password";

        SignupServiceRequest signupRequest = SignupServiceRequest.builder()
                .email(email)
                .password(password)
                .confirmPassword(password)
                .build();

        memberService.signup(signupRequest);

        LoginServiceRequest loginRequest = LoginServiceRequest.builder()
                .email(email)
                .password("wrongPassword")
                .build();

        // when // then
        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_LOGIN_ERROR);
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void reissueTokens() {
        // given
        String email = "MemberServiceTest@reissueTokens.com";

        String refreshToken = jwtProvider.createRefreshToken(email);

        redisTemplate.opsForValue().set(refreshToken, email);

        // when
        ReissueTokensResponse reissueTokensResponse = memberService.reissueTokens(refreshToken);

        // then
        assertThat(reissueTokensResponse.getAccessToken()).isNotNull();
        assertThat(reissueTokensResponse.getRefreshToken()).isNotNull();
    }

    @DisplayName("유효하지 않은 RefreshToken 사용 시 토큰을 재발급 할 수 없다.")
    @Test
    void reissueTokensWithInvalidRefreshToken() {
        // given
        String invalidRefreshToken = "invalidRefreshToken";

        // when // then
        assertThatThrownBy(() -> memberService.reissueTokens(invalidRefreshToken))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_REFRESH_TOKEN_ERROR);
    }

    @DisplayName("비밀번호를 변경한다.")
    @Test
    void changePassword() {
        // given
        String email = "test@test.com";
        String oldPassword = "oldPassword";

        SignupServiceRequest signupRequest = SignupServiceRequest.builder()
                .email(email)
                .password(oldPassword)
                .confirmPassword(oldPassword)
                .build();

        memberService.signup(signupRequest);

        AuthUser authUser = AuthUser.builder()
                .email(email)
                .build();

        String newPassword = "newPassword";
        ChangePasswordServiceRequest request = ChangePasswordServiceRequest.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();

        // when
        ChangePasswordResponse response = memberService.changePassword(authUser, request);

        // then
        assertThat(response).isNotNull();
    }

}