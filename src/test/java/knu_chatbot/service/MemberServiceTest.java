package knu_chatbot.service;

import knu_chatbot.entity.Member;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import knu_chatbot.util.EncryptionManager;
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
    private EncryptionManager encryptionManager;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("EncryptionManager에서 암호화시 기존의 password와 다른 문자가 나와야 한다.")
    @Test
    void encryptionTest() {
        // given
        String password = "password";

        // when
        String encryptPassword = encryptionManager.encrypt(password);

        // then
        assertThat(encryptPassword).isNotEqualTo(password);
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
        assertThrows(IllegalArgumentException.class, () -> memberService.signup(request))
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
        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginServiceRequest))
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
        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginServiceRequest))
            .getMessage().equals("아이디 또는 비밀번호가 맞지 않습니다.");
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