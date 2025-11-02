package knu_chatbot.infrastructure.mapper;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.infrastructure.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMapperTest {

    private final MemberMapper memberMapper = new MemberMapper();

    @DisplayName("Member 엔티티를 MemberDto로 매핑한다.")
    @Test
    void convert() {
        // given
        String email = "email";
        String password = "password";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        // when
        MemberDto memberDto = memberMapper.convert(member);

        // then
        assertThat(memberDto.getEmail()).isEqualTo(email);
        assertThat(memberDto.getPassword()).isEqualTo(password);
    }
}