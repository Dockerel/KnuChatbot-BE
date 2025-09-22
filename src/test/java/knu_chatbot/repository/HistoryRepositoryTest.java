package knu_chatbot.repository;

import knu_chatbot.config.WithContainerTest;
import knu_chatbot.entity.History;
import knu_chatbot.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HistoryRepositoryTest extends WithContainerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @DisplayName("memberId로 해당 멤버의 history를 조회한다.")
    @Test
    void findAllHistoriesByMemberId() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        int historiesCount = 2;
        historyRepository.saveAll(createHistories(member, historiesCount));

        // when
        List<History> histories = historyRepository.findAllByMemberId(member.getId());

        // then
        assertThat(histories.size()).isEqualTo(historiesCount);
    }

    private static Member createMember() {
        return Member.builder()
                .email("email@email.com")
                .build();
    }

    private static List<History> createHistories(Member member, int n) {
        List<History> histories = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            History history = History.builder()
                    .name("test")
                    .member(member)
                    .build();
            histories.add(history);
        }
        return histories;
    }
}