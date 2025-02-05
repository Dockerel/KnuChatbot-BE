package knu_chatbot.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import knu_chatbot.controller.request.MemberEmailCheckRequest;
import knu_chatbot.controller.request.MemberLoginRequest;
import knu_chatbot.controller.request.MemberSignupRequest;
import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.service.MemberService;
import knu_chatbot.service.response.MemberResponse;
import knu_chatbot.util.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/check-email")
    public ApiResponse<Object> checkEmail(@Valid @RequestBody MemberEmailCheckRequest request) {
        memberService.emailExists(request.toServiceRequest());
        return ApiResponse.ok(null);
    }

    @PostMapping("/signup")
    public ApiResponse<Object> signup(@Valid @RequestBody MemberSignupRequest request) {
        memberService.signup(request.toServiceRequest());
        return ApiResponse.ok(null);
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(
        @Valid @RequestBody MemberLoginRequest request,
        HttpServletRequest servletRequest,
        HttpServletResponse servletResponse
    ) {
        Long loginMemberId = memberService.login(request.toServiceRequest());

        HttpSession session = servletRequest.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberId);

        Cookie cookie = createCookie(session.getId());
        servletResponse.addCookie(cookie);

        return ApiResponse.ok(null);
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.ok(null);
    }

    @GetMapping("/me")
    public ApiResponse<MemberResponse> me(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Long memberId) {
        return ApiResponse.ok(memberService.getMyInfo(memberId));
    }

    @DeleteMapping
    public ApiResponse<Object> deleteMyAccount(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Long memberId) {
        memberService.deleteMyAccount(memberId);
        return ApiResponse.ok(null);
    }

    private static Cookie createCookie(String sessionId) {
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        cookie.setHttpOnly(true);  // JavaScript 접근 방지 (보안 강화)
        cookie.setSecure(true);    // HTTPS에서만 전송
        cookie.setPath("/");       // 모든 경로에서 접근 가능
        cookie.setMaxAge(60 * 30); // 1800초(30분) 유지
        return cookie;
    }
}
