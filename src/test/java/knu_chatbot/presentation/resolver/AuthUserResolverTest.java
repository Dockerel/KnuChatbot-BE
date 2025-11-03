package knu_chatbot.presentation.resolver;

import jakarta.servlet.http.HttpServletRequest;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.presentation.annotation.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthUserResolverTest {

    @InjectMocks
    private AuthUserResolver authUserResolver;

    @DisplayName("@Login 어노테이션이 있고 타입이 AuthUser면 true를 반환한다")
    @Test
    void supportsParameter() {
        // given
        MethodParameter parameter = Mockito.mock(MethodParameter.class);

        given(parameter.hasParameterAnnotation(Login.class))
                .willReturn(true);
        given(parameter.getParameterType())
                .willAnswer(invocation -> AuthUser.class);

        // when
        boolean result = authUserResolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("@Login 어노테이션이 없으면 false를 반환한다")
    @Test
    void supportsParameterWithoutAnnotation() {
        // given
        MethodParameter parameter = Mockito.mock(MethodParameter.class);

        given(parameter.hasParameterAnnotation(Login.class))
                .willReturn(false);

        // when
        boolean result = authUserResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("@Login 어노테이션이 있고 타입이 AuthUser가 아니면 false를 반환한다")
    @Test
    void supportsParameterWithoutTypeAuthUser() {
        // given
        MethodParameter parameter = Mockito.mock(MethodParameter.class);

        given(parameter.hasParameterAnnotation(Login.class))
                .willReturn(true);
        BDDMockito.<Class<?>>given(parameter.getParameterType())
                .willReturn(Object.class);

        // when
        boolean result = authUserResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("요청으로부터 AuthUser 객체를 뽑아낸다.")
    @Test
    void resolveArgument() {
        // given
        NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        given(webRequest.getNativeRequest()).willReturn(request);

        AuthUser authUser = AuthUser.builder()
                .email("test@test.com")
                .build();
        given(request.getAttribute(anyString())).willReturn(authUser);

        // when
        AuthUser result = authUserResolver.resolveArgument(null, null, webRequest, null);

        // then
        assertThat(result).isNotNull();
    }

    @DisplayName("요청에 AuthUser 객체가 있어야 한다.")
    @Test
    void resolveArgumentWithoutAuthUser() {
        // given
        NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        given(webRequest.getNativeRequest()).willReturn(request);

        // when // then
        assertThatThrownBy(() -> authUserResolver.resolveArgument(null, null, webRequest, null))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.DEFAULT_ERROR);
    }

}