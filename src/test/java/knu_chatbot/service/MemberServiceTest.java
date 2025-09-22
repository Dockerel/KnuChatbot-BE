package knu_chatbot.service;

import knu_chatbot.config.WithContainerTest;
import knu_chatbot.entity.History;
import knu_chatbot.entity.Member;
import knu_chatbot.entity.Question;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import knu_chatbot.service.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest extends WithContainerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이미 존재하는 이메일 확인 시 예외가 발생한다.")
    @Test
    void emailCheckTest() {
        // given
        String email = "email@email.com";
        Member member = createMember(email, "password");
        memberRepository.save(member);

        MemberEmailCheckServiceRequest request = MemberEmailCheckServiceRequest.builder()
                .email(email)
                .build();

        // when // then
        assertThrows(KnuChatbotException.class, () -> memberService.emailExists(request))
                .getMessage().equals("이미 존재하는 이메일입니다.");
    }

    @DisplayName("회원가입시 유저가 생성된다.")
    @Test
    void signupTest() {
        // given
        String targetEmail = "email@email.com";
        String targetNickname = "nickname";
        MemberSignupServiceRequest request = createSignupServiceRequest(targetEmail, "password", targetNickname);

        // when
        memberService.signup(request);

        Optional<Member> findMember = memberRepository.findByEmail(targetEmail);

        // then
        assertThat(findMember).isPresent().get()
                .extracting("email", "nickname")
                .contains(targetEmail, targetNickname);
    }

    @DisplayName("이미 존재하는 이메일로 회원가입할 수 없다.")
    @Test
    void signupDuplicateEmailTest() {
        // given
        String email = "email@email.com";
        Member member = createMember(email, "password");
        memberRepository.save(member);

        MemberSignupServiceRequest request = createSignupServiceRequest(email, "password", "nickname");

        // when // then
        assertThrows(KnuChatbotException.class, () -> memberService.signup(request))
                .getMessage().equals("중복된 이메일 입니다.");
    }

    @DisplayName("존재하는 이메일과 맞는 비밀번호로 로그인하면 유저Id가 반환된다.")
    @Test
    void loginTest() {
        // given
        String email = "email@email.com";
        String password = "password";

        memberService.signup(createSignupServiceRequest(email, password, "nickname"));

        Long findMemberId = memberRepository.findByEmail(email).get().getId();

        // when
        MemberLoginServiceRequest loginServiceRequest = MemberLoginServiceRequest.builder()
                .email(email)
                .password(password)
                .build();

        Long loginMemberId = memberService.login(loginServiceRequest);

        // then
        assertThat(loginMemberId).isEqualTo(findMemberId);
    }

    @DisplayName("존재하지 않는 이메일로 로그인하면 오류 메시지가 반환된다.")
    @Test
    void loginWithNonExistEmailTest() {
        // given
        String email = "email@email.com";
        String password = "password";

        memberService.signup(createSignupServiceRequest(email, password, "nickname"));

        String nonExistEmail = "nonexistEmail@email.com";

        // when
        MemberLoginServiceRequest loginServiceRequest = MemberLoginServiceRequest.builder()
                .email(nonExistEmail)
                .password(password)
                .build();

        // then
        assertThrows(KnuChatbotException.class, () -> memberService.login(loginServiceRequest))
                .getMessage().equals("아이디 또는 비밀번호가 맞지 않습니다.");
    }

    @DisplayName("올바르지 않은 비밀번호로 로그인하면 오류 메시지가 반환된다.")
    @Test
    void loginWithWrongPasswordTest() {
        // given
        String email = "email@email.com";
        String password = "password";

        memberService.signup(createSignupServiceRequest(email, password, "nickname"));

        String wrongPassword = "wrongPassword";

        // when
        MemberLoginServiceRequest loginServiceRequest = MemberLoginServiceRequest.builder()
                .email(email)
                .password(wrongPassword)
                .build();

        // then
        assertThrows(KnuChatbotException.class, () -> memberService.login(loginServiceRequest))
                .getMessage().equals("아이디 또는 비밀번호가 맞지 않습니다.");
    }

    @DisplayName("유저가 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void getMyInfoWithNonExistMemberId() {
        // given
        String email = "email@email.com";
        String password = "password";

        Member member = memberRepository.save(createMember(email, password));

        // when // then
        assertThrows(KnuChatbotException.class, () -> memberService.getMyInfo(member.getId() + 1L))
                .getMessage().equals("유저가 존재하지 않습니다.");
    }

    @DisplayName("유저가 존재하면 유저의 이메일, 닉네임, 가입날짜, 해당 유저의 질문 개수를 반환한다.")
    @Test
    void getMyInfoWithExistMemberId() {
        // given
        String email = "email@email.com";
        String password = "password";

        Member member = createMember(email, password);

        for (int i = 0; i < 100; i++) {
            History history = new History();
            for (int j = 0; j < 10; j++) {
                history.addQuestion(new Question());
            }
            member.addHistory(history);
        }

        Member savedMember = memberRepository.save(member);

        // when
        MemberResponse myInfo = memberService.getMyInfo(savedMember.getId());

        // then
        assertThat(myInfo).isNotNull()
                .extracting("email", "questionCount")
                .contains(email, 0);
    }

    @DisplayName("계정 탈퇴시 멤버 정보가 삭제되어야 한다.")
    @Test
    void deleteMyAccount() {
        // given
        String email = "email@email.com";
        String password = "password";

        Member member = memberRepository.save(createMember(email, password));

        // when
        memberService.deleteMyAccount(member.getId());

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }

    private static MemberSignupServiceRequest createSignupServiceRequest(String targetEmail, String password, String nickname) {
        return MemberSignupServiceRequest.builder()
                .email(targetEmail)
                .password(password)
                .passwordCheck(password)
                .nickname(nickname)
                .build();
    }

    private Member createMember(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname("nickname")
                .build();
    }
}