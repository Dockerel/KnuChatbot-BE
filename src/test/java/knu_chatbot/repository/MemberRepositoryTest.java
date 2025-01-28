package knu_chatbot.repository;

import knu_chatbot.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일로 멤버 정보를 조회한다.")
    @Test
    void findMemberByEmail() {
        // given
        String targetEmail = "test@email.com";
        String targetPassword = "testPassword";
        String targetNickname = "testNickname";

        Member member = Member.builder()
            .email(targetEmail)
            .password(targetPassword)
            .nickname(targetNickname)
            .build();
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findByEmail(targetEmail);

        // then
        assertThat(findMember).isEqualTo(member);
    }

}