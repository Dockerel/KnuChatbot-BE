package knu_chatbot.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.service.MemberService;
import knu_chatbot.presentation.controller.MemberController;
import knu_chatbot.presentation.interceptor.AuthInterceptor;
import knu_chatbot.presentation.resolver.AuthUserResolver;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = {MemberController.class})
public abstract class ControllerTestSupport {

    public static final String TEST_EMAIL = "test@test.com";

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockitoBean
    public AuthInterceptor authInterceptor;

    @MockitoBean
    public AuthUserResolver authUserResolver;

    @MockitoBean
    public MemberService memberService;

    @BeforeEach
    void setup() throws Exception {
        // 인터셉터 stubbing
        given(authInterceptor.preHandle(any(), any(), any()))
                .willReturn(true);

        // AuthUserResolver stubbing
        AuthUser mockAuthUser = AuthUser.builder()
                .email(TEST_EMAIL)
                .build();

        given(authUserResolver.supportsParameter(any()))
                .willReturn(true);
        given(authUserResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(mockAuthUser);
    }

}
