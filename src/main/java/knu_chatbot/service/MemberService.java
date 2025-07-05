package knu_chatbot.service;

import knu_chatbot.entity.Member;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import knu_chatbot.service.response.MemberResponse;
import knu_chatbot.util.EncryptionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final EncryptionManager encryptionManager;

    private final MemberRepository memberRepository;

    public String emailExists(MemberEmailCheckServiceRequest request) {
        String email = request.getEmail();
        if (memberExistsByEmail(email)) {
            throw new KnuChatbotException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
        return "유효한 이메일입니다.";
    }

    @Transactional
    public String signup(MemberSignupServiceRequest request) {
        String email = request.getEmail();
        if (memberExistsByEmail(email)) {
            throw new KnuChatbotException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }

        if (passwordCheckIsNotMatch(request)) {
            throw new KnuChatbotException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String encryptPassword = encryptionManager.encrypt(request.getPassword());

        Member member = Member.of(request.getEmail(), encryptPassword, request.getNickname());
        memberRepository.save(member);

        return "회원가입이 완료되었습니다.";
    }

    public Long login(MemberLoginServiceRequest request) {
        String encryptPassword = encryptionManager.encrypt(request.getPassword());

        Member findMember = findMemberByEmail(request.getEmail());

        if (passwordIsNotMatch(findMember, encryptPassword)) {
            throw new KnuChatbotException("아이디 또는 비밀번호가 맞지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return findMember.getId();
    }

    public MemberResponse getMyInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return MemberResponse.of(member, member.getQuestionCount());
    }

    @Transactional
    public void deleteMyAccount(Long memberId) {
        if (memberId == null) return;
        memberRepository.deleteById(memberId);
    }

    private static boolean passwordCheckIsNotMatch(MemberSignupServiceRequest request) {
        return !request.getPassword().equals(request.getPasswordCheck());
    }

    private boolean passwordIsNotMatch(Member findMember, String encryptPassword) {
        return findMember == null || !findMember.getPassword().equals(encryptPassword);
    }

    private boolean memberExistsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new KnuChatbotException("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new KnuChatbotException("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }

}

