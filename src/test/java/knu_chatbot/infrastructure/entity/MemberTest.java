package knu_chatbot.infrastructure.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("비밀번호를 변경한다")
    @Test
    void changePassword() {
        // given
        String oldPassword = "test123";
        String newPassword = "newTest123";

        Member member = Member.builder()
                .email("test@test.com")
                .password(oldPassword)
                .build();

        // when
        member.changePassword(newPassword);

        // then
        assertThat(member.getPassword()).isEqualTo(newPassword);
    }
}