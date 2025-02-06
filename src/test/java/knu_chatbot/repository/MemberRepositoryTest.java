package knu_chatbot.repository;

import knu_chatbot.entity.History;
import knu_chatbot.entity.Member;
import knu_chatbot.entity.Question;
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

        Member member = createMember(targetEmail, targetPassword, targetNickname);
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findByEmail(targetEmail);

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @DisplayName("회원이 한 질문 수를 조회한다.")
    @Test
    void countByMemberId() {
        // given
        Member member1 = createMember("test@test.com", "testpw", "testnickname");
        History history1 = new History();
        for (int i = 0; i < 10; i++) {
            history1.addQuestion(new Question());
            history1.addQuestion(new Question());
        }
        member1.addHistory(history1);
        Member saveMember = memberRepository.save(member1);

        Member member2 = createMember("test2@test.com", "testpw2", "testnickname2");
        History history2 = new History();
        for (int i = 0; i < 10; i++) {
            history2.addQuestion(new Question());
            history2.addQuestion(new Question());
            history2.addQuestion(new Question());
        }
        member2.addHistory(history2);
        memberRepository.save(member2);


        // when
        int result = memberRepository.countByMemberId(saveMember.getId());

        // then
        assertThat(result).isEqualTo(20);
    }

    private static Member createMember(String targetEmail, String targetPassword, String targetNickname) {
        return Member.builder()
            .email(targetEmail)
            .password(targetPassword)
            .nickname(targetNickname)
            .build();
    }

}