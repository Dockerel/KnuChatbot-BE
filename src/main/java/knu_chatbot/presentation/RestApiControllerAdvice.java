package knu_chatbot.presentation;

import knu_chatbot.application.error.ErrorMessage;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestApiControllerAdvice {

    @ExceptionHandler(KnuChatbotException.class)
    public ResponseEntity<ApiResponse<Object>> knuChatbotException(KnuChatbotException e) {
        ApiResponse<Object> response = ApiResponse.error(new ErrorMessage(e.getErrorType(), e.getData()));
        return new ResponseEntity<>(response, e.getErrorType().getStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> bindException(BindException e) {
        ApiResponse<Object> response = ApiResponse.error(new ErrorMessage(ErrorType.BINDING_ERROR, e.getBindingResult().getFieldError().getDefaultMessage()));
        return new ResponseEntity<>(response, ErrorType.BINDING_ERROR.getStatus());
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ApiResponse<Object>> servletRequestBindingException(ServletRequestBindingException e) {
        ApiResponse<Object> response = ApiResponse.error(new ErrorMessage(ErrorType.USER_LOGIN_REQUIRED_ERROR));
        return new ResponseEntity<>(response, ErrorType.BINDING_ERROR.getStatus());
    }

}
