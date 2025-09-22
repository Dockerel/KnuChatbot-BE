package knu_chatbot.service;

import jakarta.persistence.EntityManager;
import knu_chatbot.config.WithContainerTest;
import knu_chatbot.controller.response.HistoryResponse;
import knu_chatbot.controller.response.QuestionAndAnswerResponse;
import knu_chatbot.entity.*;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.repository.HistoryRepository;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.UpdateHistoryNameServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class HistoryServiceTest extends WithContainerTest {

    @Autowired
    HistoryService historyService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    EntityManager em;

    @DisplayName("멤버 id로 모든 history를 가져온다.")
    @Test
    void getAllHistoriesByMemberIdTest() {
        // given
        Member member = Member.of("email", "password", "nickname");
        History history = History.of("historyName");
        member.addHistory(history);

        Member saveMember = memberRepository.save(member);

        // when
        List<HistoryResponse> histories = historyService.getAllHistoriesByMemberId(saveMember.getId());

        // then
        assertThat(histories).hasSize(1);
    }

    @DisplayName("유효한 멤버id와 히스토리id로 모든 질문들과 답변들을 가져온다.")
    @Test
    void getAllQuestionsAndAnswersWithValidMemberIdAndHistoryIdTest() {
        // given
        int expectedQuestionNumber = 10;

        Member member = Member.of("email", "password", "nickname");
        History history = History.of("historyName");

        for (int i = 0; i < expectedQuestionNumber; i++) {
            Question question = Question.of("text");
            Answer answer = Answer.of("text", "reference");

            answer.addImage(Image.of("image"));
            answer.addImage(Image.of("image"));

            question.setAnswer(answer);
            history.addQuestion(question);
        }
        History saveHistory = historyRepository.save(history);
        member.addHistory(history);
        Member saveMember = memberRepository.save(member);

        em.flush();
        em.clear();

        // when
        List<QuestionAndAnswerResponse> questionsAndAnswers = historyService.getAllQuestionsAndAnswers(saveMember.getId(), saveHistory.getId());

        // then
        assertThat(questionsAndAnswers).hasSize(expectedQuestionNumber);
    }

    @DisplayName("유효하지 않은 히스토리id로 요청 시 예외가 발생한다.")
    @Test
    void getAllQuestionsAndAnswersWithInvalidHistoryIdTest() {
        // given
        Long invalidHistoryId = -1L;

        // when // then
        assertThatThrownBy(() -> historyService.getAllQuestionsAndAnswers(1L, invalidHistoryId))
                .isInstanceOf(KnuChatbotException.class)
                .hasMessage("히스토리가 존재하지 않습니다.");
    }

    @DisplayName("히스토리의 주인이 아닌 멤버id로 요청 시 예외가 발생한다.")
    @Test
    void getAllQuestionsAndAnswersWithInvalidMemberIdTest() {
        // given
        Long invalidMemberId = -1L;

        Member member = Member.of("email", "password", "nickname");

        History history = History.of("historyName");
        History saveHistory = historyRepository.save(history);

        member.addHistory(history);
        memberRepository.save(member);

        // when // then
        assertThatThrownBy(() -> historyService.getAllQuestionsAndAnswers(invalidMemberId, saveHistory.getId()))
                .isInstanceOf(KnuChatbotException.class)
                .hasMessage("히스토리 주인이 아닙니다.");
    }

    @DisplayName("유효한 멤버id와 히스토리id로 히스토리의 이름을 변경한다.")
    @Test
    void updateHistoryTest() {
        // given
        String updatedHistoryName = "updatedHistoryName";

        Member member = Member.of("email", "password", "nickname");
        History saveHistory = historyRepository.save(History.of("historyName"));
        member.addHistory(saveHistory);
        Member saveMember = memberRepository.save(member);

        Long memberId = saveMember.getId();
        Long historyId = saveHistory.getId();

        // when
        historyService.updateHistory(memberId, historyId, UpdateHistoryNameServiceRequest.of(updatedHistoryName));
        em.flush();
        em.clear();

        Optional<History> findHistory = historyRepository.findById(historyId);

        // then
        assertThat(findHistory).isPresent()
                .get().extracting(History::getName).isEqualTo(updatedHistoryName);
    }

    @DisplayName("히스토리의 주인이 아닌 멤버id로 요청 시 예외가 발생한다.")
    @Test
    void updateHistoryWithInvalidMemberIdTest() {
        // given
        Member member = Member.of("email", "password", "nickname");
        History saveHistory = historyRepository.save(History.of("historyName"));
        member.addHistory(saveHistory);
        Member saveMember = memberRepository.save(member);

        Long memberId = saveMember.getId();
        Long historyId = saveHistory.getId();

        // when // then
        assertThatThrownBy(() -> historyService.updateHistory(memberId + 1L, historyId, UpdateHistoryNameServiceRequest.of("updatedHistoryName")))
                .isInstanceOf(KnuChatbotException.class)
                .hasMessage("히스토리 주인이 아닙니다.");
    }

    @DisplayName("유효한 멤버id와 히스토리id로 히스토리를 삭제한다.")
    @Test
    void deleteHistoryTest() {
        // given
        Member member = Member.of("email", "password", "nickname");
        History saveHistory = historyRepository.save(History.of("historyName"));
        member.addHistory(saveHistory);
        Member saveMember = memberRepository.save(member);

        Long memberId = saveMember.getId();
        Long historyId = saveHistory.getId();

        // when
        historyService.deleteHistory(memberId, historyId);
        em.flush();
        em.clear();

        Optional<History> findHistory = historyRepository.findById(historyId);

        // then
        assertThat(findHistory).isEmpty();
    }

    @DisplayName("히스토리의 주인이 아닌 멤버id로 요청 시 예외가 발생한다.")
    @Test
    void deleteHistoryWithInvalidMemberIdTest() {
        // given
        Member member = Member.of("email", "password", "nickname");
        History saveHistory = historyRepository.save(History.of("historyName"));
        member.addHistory(saveHistory);
        Member saveMember = memberRepository.save(member);

        Long memberId = saveMember.getId();
        Long historyId = saveHistory.getId();

        // when // then
        assertThatThrownBy(() -> historyService.deleteHistory(memberId + 1L, historyId))
                .isInstanceOf(KnuChatbotException.class)
                .hasMessage("히스토리 주인이 아닙니다.");
    }

}