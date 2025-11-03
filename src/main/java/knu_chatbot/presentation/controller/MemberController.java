package knu_chatbot.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.dto.response.*;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.service.MemberService;
import knu_chatbot.application.util.JwtProvider;
import knu_chatbot.presentation.ApiResponse;
import knu_chatbot.presentation.annotation.Login;
import knu_chatbot.presentation.request.ChangePasswordRequest;
import knu_chatbot.presentation.request.CheckEmailRequest;
import knu_chatbot.presentation.request.LoginRequest;
import knu_chatbot.presentation.request.SignupRequest;
import knu_chatbot.presentation.response.AccessTokenResponse;
import knu_chatbot.presentation.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import static knu_chatbot.application.util.AuthConst.REFRESH_TOKEN;
import static knu_chatbot.application.util.AuthConst.SET_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "멤버", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "이메일 중복 체크",
            description = "이메일이 중복인지 체크한다."
    )
    @PostMapping("/check-email")
    public ApiResponse<CheckEmailResponse> checkEmail(@Valid @RequestBody CheckEmailRequest request) {
        return ApiResponse.success(memberService.checkEmail(request.toServiceRequest()));
    }

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행한다."
    )
    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.success(memberService.signup(request.toServiceRequest()));
    }

    @Operation(
            summary = "로그인",
            description = "로그인한다."
    )
    @PostMapping("/login")
    public ApiResponse<AccessTokenResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = memberService.login(request.toServiceRequest());

        ResponseCookie cookie = createCookie(loginResponse.getRefreshToken());

        response.setHeader(SET_COOKIE, cookie.toString());

        return ApiResponse.success(loginResponse.toAccessTokenResponse());
    }

    @Operation(
            summary = "Token 재발급",
            description = "Token을 재발급한다."
    )
    @PostMapping("/reissue")
    public ApiResponse<AccessTokenResponse> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);

        ReissueTokensResponse reissueTokensResponse = memberService.reissueTokens(refreshToken);

        ResponseCookie cookie = createCookie(reissueTokensResponse.getRefreshToken());

        response.setHeader(SET_COOKIE, cookie.toString());

        return ApiResponse.success(reissueTokensResponse.toAccessTokenResponse());
    }

    @Operation(
            summary = "마이페이지",
            description = "마이페이지 정보를 보여준다."
    )
    @GetMapping("/me")
    public ApiResponse<MyPageResponse> getMyPage(@Parameter(hidden = true) @Login AuthUser authUser) {
        return ApiResponse.success(memberService.getMyPage(authUser.getEmail()));
    }

    // TODO : 로그아웃
    @Operation(
            summary = "로그아웃",
            description = "로그아웃 한다."
    )
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);
        return ApiResponse.success(memberService.logout(refreshToken));
    }

    @Operation(
            summary = "회원 탈퇴"
    )
    @DeleteMapping
    public ApiResponse<WithdrawResponse> withdraw(@Parameter(hidden = true) @Login AuthUser authUser) {
        return ApiResponse.success(memberService.withdraw(authUser.getEmail()));
    }

    // TODO : 비밀번호 변경
    @Operation(
            summary = "비밀번호 변경",
            description = "비밀번호를 수정한다."
    )
    @PatchMapping
    public ApiResponse<ChangePasswordResponse> changePassword(
            @Parameter(hidden = true) @Login AuthUser authUser,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        return ApiResponse.success(memberService.changePassword(authUser, request.toServiceRequest()));
    }

    // TODO : 회원 정보 수정 (현재는 수정할 정보가 없음)

    private ResponseCookie createCookie(String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(JwtProvider.REFRESH_TOKEN_EXPIRE_SECONDS)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();
        return cookie;
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN)) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new KnuChatbotException(ErrorType.USER_COOKIE_REFRESH_TOKEN_ERROR);
        }

        return refreshToken;
    }
}
