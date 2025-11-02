package knu_chatbot.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import knu_chatbot.application.service.MemberService;
import knu_chatbot.presentation.controller.MemberController;
import knu_chatbot.presentation.interceptor.AuthInterceptor;
import knu_chatbot.presentation.resolver.AuthUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MemberController.class})
public abstract class ControllerTestSupport {

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

}
