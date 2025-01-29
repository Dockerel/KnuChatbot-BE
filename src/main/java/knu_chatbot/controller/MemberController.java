package knu_chatbot.controller;

import jakarta.validation.Valid;
import knu_chatbot.controller.request.MemberEmailCheckRequest;
import knu_chatbot.controller.request.MemberSignupRequest;
import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
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
}
