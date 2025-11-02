package knu_chatbot.application.service;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.application.dto.request.CheckEmailServiceRequest;
import knu_chatbot.application.dto.request.LoginServiceRequest;
import knu_chatbot.application.dto.request.SignupServiceRequest;
import knu_chatbot.application.dto.response.CheckEmailResponse;
import knu_chatbot.application.dto.response.LoginResponse;
import knu_chatbot.application.dto.response.ReissueTokensResponse;
import knu_chatbot.application.dto.response.SignupResponse;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.repository.MemberRepository;
import knu_chatbot.application.util.JwtProvider;
import knu_chatbot.application.util.PasswordEncryptor;
import knu_chatbot.infrastructure.entity.Member;
import knu_chatbot.presentation.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncryptor passwordEncryptor;

    private final JwtProvider jwtProvider;

    public CheckEmailResponse checkEmail(CheckEmailServiceRequest request) {
        boolean result = memberRepository.existsByEmail(request.getEmail());
        return CheckEmailResponse.of(!result);
    }

    @Transactional
    public SignupResponse signup(SignupServiceRequest request) {
        validatePasswordMatch(request);

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new KnuChatbotException(ErrorType.USER_INVALID_EMAIL_ERROR, request.getEmail());
        }

        String encryptedPassword = passwordEncryptor.encryptPassword(request.getPassword());

        Member member = Member.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .build();
        memberRepository.save(member);

        return SignupResponse.of("회원가입이 완료되었습니다.");
    }

    public LoginResponse login(LoginServiceRequest request) {
        MemberDto memberDto = memberRepository.findByEmail(request.getEmail());

        // 비밀번호 검증
        if (!passwordEncryptor.verifyPassword(request.getPassword(), memberDto.getPassword())) {
            throw new KnuChatbotException(ErrorType.USER_LOGIN_ERROR);
        }

        // accessToken 생성
        String accessToken = jwtProvider.createAccessToken(memberDto.getEmail());

        // refreshToken 생성 및 저장
        String refreshToken = jwtProvider.createRefreshToken(memberDto.getEmail());
        memberRepository.saveRefreshToken(refreshToken, memberDto.getEmail(), JwtProvider.REFRESH_TOKEN_EXPIRE_SECONDS);

        return LoginResponse.of(accessToken, refreshToken);
    }

    public ReissueTokensResponse reissueTokens(String refreshToken) {
        // jwt 검증
        jwtProvider.validateToken(refreshToken);

        // redis의 refreshToken 검증
        String email = memberRepository.getEmailByRefreshToken(refreshToken);
        // 이전 refreshToken 삭제
        memberRepository.deleteRefreshToken(refreshToken);

        // accessToken 생성
        String newAccessToken = jwtProvider.createAccessToken(email);

        // refreshToken 생성 및 저장
        String newRefreshToken = jwtProvider.createRefreshToken(email);
        memberRepository.saveRefreshToken(newRefreshToken, email, JwtProvider.REFRESH_TOKEN_EXPIRE_SECONDS);

        return ReissueTokensResponse.of(newAccessToken, newRefreshToken);
    }

    public MyPageResponse getMyPage(String email) {
        MemberDto memberDto = memberRepository.findByEmail(email);
        return MyPageResponse.from(memberDto);
    }

    private void validatePasswordMatch(SignupServiceRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new KnuChatbotException(ErrorType.USER_CONFIRM_PASSWORD_ERROR, request);
        }
    }

}

