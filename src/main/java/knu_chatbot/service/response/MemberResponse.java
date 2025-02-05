package knu_chatbot.service.response;

import knu_chatbot.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {

    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private int questionCount;

    @Builder
    public MemberResponse(String email, String nickname, LocalDateTime createdAt, int questionCount) {
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.questionCount = questionCount;
    }

    public static MemberResponse of(Member member, int questionCount) {
        return MemberResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedAt())
            .questionCount(questionCount)
            .build();
    }
}
