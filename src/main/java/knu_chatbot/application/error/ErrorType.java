package knu_chatbot.application.error;

import io.netty.handler.logging.LogLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    BINDING_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "요청이 올바르지 않습니다.", LogLevel.INFO),

    // 유저
    USER_INVALID_EMAIL_ERROR(HttpStatus.CONFLICT, ErrorCode.E1000, "이미 존재하는 이메일입니다.", LogLevel.INFO),
    USER_CONFIRM_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E1001, "비밀번호 확인이 일치하지 않습니다.", LogLevel.INFO),
    USER_LOGIN_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1002, "아이디 또는 비밀번호가 맞지 않습니다.", LogLevel.INFO),
    USER_INVALID_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1003, "유저가 존재하지 않습니다.", LogLevel.INFO),
    USER_NOT_HISTORY_OWNER_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1004, "히스토리 주인이 아닙니다.", LogLevel.INFO),
    USER_LOGIN_REQUIRED_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1005, "로그인이 필요합니다.", LogLevel.INFO),
    USER_COOKIE_REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1006, "쿠키에 RefreshToken이 없습니다.", LogLevel.INFO),
    USER_INVALID_REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1007, "유효하지 않은 RefreshToken 입니다.", LogLevel.INFO),
    USER_INVALID_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E1008, "유효하지 않은 AccessToken 입니다.", LogLevel.INFO),

    // 히스토리
    HISTORY_INVALID_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E2000, "히스토리가 존재하지 않습니다.", LogLevel.INFO);


    private final HttpStatus status;
    private final ErrorCode errorType;
    private final String message;
    private final LogLevel logLevel;

}
