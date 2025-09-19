package knu_chatbot.advice;

import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.util.DiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestApiControllerAdvice {

    private final DiscordClient discordClient;

    @ExceptionHandler(KnuChatbotException.class)
    public ApiResponse<Object> knuChatbotException(KnuChatbotException e) {
        ApiResponse<Object> response = ApiResponse.of(
                e.getHttpStatus(),
                e.getMessage(),
                null
        );
        saveAndSendErrorMessage(response, e);
        return response;
    }

    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        ApiResponse<Object> response = ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
        saveAndSendErrorMessage(response, e);
        return response;
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiResponse<Object> servletRequestBindingException(ServletRequestBindingException e) {
        ApiResponse<Object> response = ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "로그인이 필요하거나, 세션 정보가 올바르지 않습니다.",
                null
        );
        saveAndSendErrorMessage(response, e);
        return response;
    }

    private void saveAndSendErrorMessage(ApiResponse<Object> response, Exception e) {
        String errorId = UUID.randomUUID().toString();
        log.error("errorId : {} : ", errorId, e);
        discordClient.sendErrorMessage(response, errorId);
    }

}
