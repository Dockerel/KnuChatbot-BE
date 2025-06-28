package knu_chatbot.advice;

import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.exception.KnuChatbotException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiControllerAdvice {

    @ExceptionHandler(KnuChatbotException.class)
    public ApiResponse<Object> knuChatbotException(KnuChatbotException e) {
        return ApiResponse.of(
                e.getHttpStatus(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiResponse<Object> servletRequestBindingException(ServletRequestBindingException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "로그인이 필요하거나, 세션 정보가 올바르지 않습니다.",
                null
        );
    }

}
