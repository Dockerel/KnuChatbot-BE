package knu_chatbot.service;

import knu_chatbot.annotation.CheckHistoryOwner;
import knu_chatbot.controller.response.AnswerResponse;
import knu_chatbot.controller.response.HistoryResponse;
import knu_chatbot.controller.response.QuestionAndAnswerResponse;
import knu_chatbot.controller.response.QuestionResponse;
import knu_chatbot.entity.Answer;
import knu_chatbot.entity.History;
import knu_chatbot.entity.Member;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.repository.AnswerRepository;
import knu_chatbot.repository.HistoryRepository;
import knu_chatbot.repository.MemberRepository;
import knu_chatbot.service.request.UpdateHistoryNameServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    public List<HistoryResponse> getAllHistoriesByMemberId(Long memberId) {
        return historyRepository.findAllByMemberId(memberId).stream()
                .map(history -> HistoryResponse.of(history))
                .collect(Collectors.toList());
    }

    @Transactional
    public HistoryResponse createHistory(Long memberId) {
        Member member = findMemberById(memberId);

        History history = History.of(UUID.randomUUID().toString());
        member.addHistory(history);

        return HistoryResponse.of(historyRepository.save(history));
    }

    @CheckHistoryOwner
    public List<QuestionAndAnswerResponse> getAllQuestionsAndAnswers(Long memberId, Long historyId) {
        History history = findHistoryWithQuestionsAndAnswersAndImages(historyId);

        return history.getQuestions().stream()
                .map(question -> {
                    QuestionResponse questionResponse = QuestionResponse.of(question.getText());

                    Answer answer = question.getAnswer();
                    List<String> images = answer.getImages().stream()
                            .map(image -> image.getEncodedImage())
                            .collect(Collectors.toList());

                    AnswerResponse answerResponse = AnswerResponse.of(answer, images);

                    return QuestionAndAnswerResponse.of(questionResponse, answerResponse);
                })
                .collect(Collectors.toList());
    }

    @CheckHistoryOwner
    @Transactional
    public HistoryResponse updateHistory(Long memberId, Long historyId, UpdateHistoryNameServiceRequest request) {
        History history = findHistoryById(historyId);
        history.changeName(request.getName());
        return HistoryResponse.of(history);
    }

    @CheckHistoryOwner
    @Transactional
    public String deleteHistory(Long memberId, Long historyId) {
        Member member = findMemberById(memberId);
        member.removeHistory(historyId);
        return "히스토리가 삭제되었습니다.";
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new KnuChatbotException("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }

    private History findHistoryWithQuestionsAndAnswersAndImages(Long historyId) {
        History history = historyRepository.findHistoryWithQuestionsAndAnswers(historyId);

        List<Answer> answers = history.getQuestions().stream()
                .map(question -> question.getAnswer())
                .collect(Collectors.toList());

        answerRepository.findAllAnswersWithImages(answers);

        return history;
    }

    private History findHistoryById(Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new KnuChatbotException("히스토리가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }
}
