package knu_chatbot.aop;

import knu_chatbot.entity.History;
import knu_chatbot.exception.KnuChatbotException;
import knu_chatbot.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Aspect
@Component
@RequiredArgsConstructor
public class HistoryOwnerCheckAspect {

    private final HistoryRepository historyRepository;

    @Before("@annotation(knu_chatbot.annotation.CheckHistoryOwner)")
    public void checkHistoryOwner(JoinPoint joinPoint) {
        Long memberId = findParameterValue(joinPoint, "memberId", Long.class);
        Long historyId = findParameterValue(joinPoint, "historyId", Long.class);

        History historyCheck = findHistoryById(historyId);

        Long historyOwner = historyCheck.getMember().getId();
        if (!historyOwner.equals(memberId)) {
            throw new KnuChatbotException("히스토리 주인이 아닙니다.", HttpStatus.FORBIDDEN);
        }
    }

    private <T> T findParameterValue(JoinPoint joinPoint, String parameterName, Class<T> type) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(parameterName)) {
                return type.cast(args[i]);
            }
        }

        throw new KnuChatbotException("AOP: @" + signature.getMethod().getName() + " 메서드에서 '" + parameterName + "' 파라미터를 찾을 수 없습니다.", INTERNAL_SERVER_ERROR);
    }

    public History findHistoryById(Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new KnuChatbotException("히스토리가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }
}
