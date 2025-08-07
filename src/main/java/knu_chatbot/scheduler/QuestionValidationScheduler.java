package knu_chatbot.scheduler;

import knu_chatbot.entity.Member;
import knu_chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Component
public class QuestionValidationScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시
    public void validateQuestionCounts() {
        long start = System.currentTimeMillis();
        List<Member> members = memberService.findAllMembers();

        List<CompletableFuture<Void>> futures =
                members.stream()
                        .map(memberService::validateQuestionCount)
                        .toList();
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
