package knu_chatbot.infrastructure.mapper;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.infrastructure.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDto convert(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
