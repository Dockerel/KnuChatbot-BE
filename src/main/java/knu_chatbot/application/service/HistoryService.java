package knu_chatbot.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class HistoryService {
//
//    private final HistoryRepository historyRepository;
//    private final MemberJpaRepository memberRepository;
//
//    public List<HistoryResponse> getAllHistoriesByMemberId(Long memberId) {
//        return historyRepository.findAllByMemberId(memberId).stream()
//                .map(history -> HistoryResponse.of(history))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public HistoryResponse createHistory(Long memberId) {
//        Member member = findMemberById(memberId);
//
//        History history = History.of(UUID.randomUUID().toString());
//        member.addHistory(history);
//
//        return HistoryResponse.of(historyRepository.save(history));
//    }
//
//    @CheckHistoryOwner
//    public List<QuestionAndAnswerResponse> getAllQuestionsAndAnswers(Long memberId, Long historyId) {
//        History history = findHistoryWithQuestionsAndAnswersAndImages(historyId);
//
//        return history.getQuestions().stream()
//                .map(question -> {
//                    QuestionResponse questionResponse = QuestionResponse.of(question.getText());
//
//                    Answer answer = question.getAnswer();
//                    List<String> images = answer.getImages().stream()
//                            .map(image -> image.getEncodedImage())
//                            .collect(Collectors.toList());
//
//                    AnswerResponse answerResponse = AnswerResponse.of(answer, images);
//
//                    return QuestionAndAnswerResponse.of(questionResponse, answerResponse);
//                })
//                .collect(Collectors.toList());
//    }
//
//    @CheckHistoryOwner
//    @Transactional
//    public CreateNewChatResponse createNewQuestion(Long memberId, Long historyId, CreateNewQuestionRequest request) {
//        // member question count 갱신
//        return null;
//    }
//
//    @CheckHistoryOwner
//    @Transactional
//    public HistoryResponse updateHistory(Long memberId, Long historyId, UpdateHistoryNameServiceRequest request) {
//        History history = findHistoryById(historyId);
//        history.changeName(request.getName());
//        return HistoryResponse.of(history);
//    }
//
//    @CheckHistoryOwner
//    @Transactional
//    public String deleteHistory(Long memberId, Long historyId) {
//        Member member = findMemberById(memberId);
//        member.removeHistory(historyId);
//        return "히스토리가 삭제되었습니다.";
//    }
//
//    private Member findMemberById(Long memberId) {
//        return memberRepository.findById(memberId)
//                .orElseThrow(() -> new KnuChatbotException(ErrorType.USER_INVALID_ERROR));
//    }
//
//    private History findHistoryWithQuestionsAndAnswersAndImages(Long historyId) {
//        return historyRepository.findHistoryWithQuestionsAndAnswers(historyId);
//    }
//
//    private History findHistoryById(Long historyId) {
//        return historyRepository.findById(historyId)
//                .orElseThrow(() -> new KnuChatbotException(ErrorType.HISTORY_INVALID_ERROR));
//    }
}
