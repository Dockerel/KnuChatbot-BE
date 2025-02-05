package knu_chatbot.service;

import knu_chatbot.entity.Member;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import knu_chatbot.service.response.MemberResponse;
import knu_chatbot.util.EncryptionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final EncryptionManager encryptionManager;

    public void emailExists(MemberEmailCheckServiceRequest request) {
        String email = request.getEmail();
        Member findMember = memberRepository.findByEmail(email);
        if (findMember != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional
    public void signup(MemberSignupServiceRequest request) {
        Member findMember = memberRepository.findByEmail(request.getEmail());
        if (findMember != null) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        String encryptPassword = encryptionManager.encrypt(request.getPassword());
        Member member = request.toEntity(encryptPassword);
        memberRepository.save(member);
    }

    public Long login(MemberLoginServiceRequest request) {

        String encryptPassword = encryptionManager.encrypt(request.getPassword());

        Member findMember = memberRepository.findByEmail(request.getEmail());

        if (findMemberNotMatchPassword(findMember, encryptPassword)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않습니다.");
        }

        return findMember.getId();
    }

    public MemberResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        int questionCount = member.getHistories().stream()
            .mapToInt(h -> h.getQuestions().size())
            .sum();

        return MemberResponse.of(member, questionCount);
    }

    private boolean findMemberNotMatchPassword(Member findMember, String encryptPassword) {
        return findMember == null || !findMember.getPassword().equals(encryptPassword);
    }
}

