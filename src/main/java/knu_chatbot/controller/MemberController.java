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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static knu_chatbot.util.SessionConst.LOGIN_MEMBER;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    public static final String JSESSIONID = "JSESSIONID";

    private final MemberService memberService;

    @PostMapping("/check-email")
    public ApiResponse<String> checkEmail(@Valid @RequestBody MemberEmailCheckRequest request) {
        return ApiResponse.ok(memberService.emailExists(request.toServiceRequest()));
    }

    @PostMapping("/signup")
    public ApiResponse<String> signup(@Valid @RequestBody MemberSignupRequest request) {
        return ApiResponse.ok(memberService.signup(request.toServiceRequest()));
    }

    @PostMapping("/login")
    public ApiResponse<String> login(
            @Valid @RequestBody MemberLoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        Long loginMemberId = memberService.login(request.toServiceRequest());

        HttpSession session = servletRequest.getSession();
        session.setAttribute(LOGIN_MEMBER, loginMemberId);

        Cookie cookie = createCookie(session.getId());
        servletResponse.addCookie(cookie);

        return ApiResponse.ok("로그인이 완료되었습니다.");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.ok("로그아웃이 완료되었습니다.");
    }

    @GetMapping("/me")
    public ApiResponse<MemberResponse> me(@SessionAttribute(name = LOGIN_MEMBER) Long memberId) {
        return ApiResponse.ok(memberService.getMyInfo(memberId));
    }

    @DeleteMapping
    public ApiResponse<String> deleteMyAccount(@SessionAttribute(name = LOGIN_MEMBER) Long memberId) {
        memberService.deleteMyAccount(memberId);
        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.");
    }

    private static Cookie createCookie(String sessionId) {
        Cookie cookie = new Cookie(JSESSIONID, sessionId);
        cookie.setHttpOnly(true);  // JavaScript 접근 방지 (보안 강화)
        cookie.setSecure(true);    // HTTPS에서만 전송
        cookie.setPath("/");       // 모든 경로에서 접근 가능
        cookie.setMaxAge(60 * 30); // 1800초(30분) 유지
        return cookie;
    }
}
