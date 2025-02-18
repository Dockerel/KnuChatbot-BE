package knu_chatbot.service;

import knu_chatbot.entity.History;
import knu_chatbot.entity.Member;
import knu_chatbot.entity.Question;
import knu_chatbot.exception.MyAuthenticationException;
import knu_chatbot.repository.HistoryRepository;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.repository.QuestionRepository;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import knu_chatbot.service.response.MemberResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @AfterEach
    void tearDown() {
        questionRepository.deleteAllInBatch();
        historyRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

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
        assertThrows(MyAuthenticationException.class, () -> memberService.emailExists(request))
            .getMessage().equals("이미 존재하는 이메일입니다.");
    }

    @DisplayName("회원가입시 유저가 생성된다.")
    @Test
    void signupTest() {
        // given
        String targetEmail = "email@email.com";
        MemberSignupServiceRequest request = createSignupServiceRequest(targetEmail, "password");

        // when
        memberService.signup(request);

        Member findMember = memberRepository.findByEmail(targetEmail);

        // then
        assertThat(findMember).isNotNull()
            .extracting("email", "nickname")
            .contains(targetEmail, findMember.getNickname());
    }

    @DisplayName("이미 존재하는 이메일로 회원가입할 수 없다.")
    @Test
    void signupDuplicateEmailTest() {
        // given
        String email = "email@email.com";
        Member member = createMember(email, "password");
        memberRepository.save(member);

        MemberSignupServiceRequest request = createSignupServiceRequest(email, "password");

        // when // then
        assertThrows(MyAuthenticationException.class, () -> memberService.signup(request))
            .getMessage().equals("중복된 이메일 입니다.");
    }

    @DisplayName("존재하는 이메일과 맞는 비밀번호로 로그인하면 유저Id가 반환된다.")
    @Test
    void loginTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        Member member = createMember(email, password);

        memberService.signup(createSignupServiceRequest(email, password));

        Long findMemberId = memberRepository.findByEmail(email).getId();

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

        memberService.signup(createSignupServiceRequest(email, password));

        String nonExistEmail = "nonexistEmail@email.com";

        // when
        MemberLoginServiceRequest loginServiceRequest = MemberLoginServiceRequest.builder()
            .email(nonExistEmail)
            .password(password)
            .build();

        // then
        assertThrows(MyAuthenticationException.class, () -> memberService.login(loginServiceRequest))
            .getMessage().equals("아이디 또는 비밀번호가 맞지 않습니다.");
    }

    @DisplayName("올바르지 않은 비밀번호로 로그인하면 오류 메시지가 반환된다.")
    @Test
    void loginWithWrongPasswordTest() {
        // given
        String email = "email@email.com";
        String password = "password";

        memberService.signup(createSignupServiceRequest(email, password));

        String wrongPassword = "wrongPassword";

        // when
        MemberLoginServiceRequest loginServiceRequest = MemberLoginServiceRequest.builder()
            .email(email)
            .password(wrongPassword)
            .build();

        // then
        assertThrows(MyAuthenticationException.class, () -> memberService.login(loginServiceRequest))
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
        assertThrows(MyAuthenticationException.class, () -> memberService.getMyInfo(member.getId() + 1L))
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
            .contains(email, 1000);
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

    private static MemberSignupServiceRequest createSignupServiceRequest(String targetEmail, String password) {
        return MemberSignupServiceRequest.builder()
            .email(targetEmail)
            .password(password)
            .nickname("nickname")
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