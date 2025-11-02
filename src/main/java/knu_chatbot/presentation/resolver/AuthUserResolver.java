package knu_chatbot.presentation.resolver;

import jakarta.servlet.http.HttpServletRequest;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.presentation.annotation.Login;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static knu_chatbot.application.util.AuthConst.AUTHENTICATED_USER;

@Component
public class AuthUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class)
                && parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public AuthUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        AuthUser authUser = (AuthUser) request.getAttribute(AUTHENTICATED_USER);

        if (authUser == null) {
            throw new KnuChatbotException(ErrorType.DEFAULT_ERROR);
        }

        return authUser;
    }

}
