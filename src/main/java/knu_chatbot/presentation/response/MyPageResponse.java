package knu_chatbot.presentation.response;

import knu_chatbot.application.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponse {

    private String email;

    @Builder
    public MyPageResponse(String email) {
        this.email = email;
    }

    public static MyPageResponse from(MemberDto memberDto) {
        return MyPageResponse.builder()
                .email(memberDto.getEmail())
                .build();
    }
}
