package knu_chatbot.infrastructure.repository;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.infrastructure.entity.Member;
import knu_chatbot.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepositoryImpl memberRepositoryImpl;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @DisplayName("존재하는 이메일로 멤버를 찾아서 DTO를 생성한다.")
    @Test
    void findByEmail() {
        // given
        String email = "email";
        String password = "password";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        memberJpaRepository.save(member);

        // when
        MemberDto memberDto = memberRepositoryImpl.findByEmail(email);

        // then
        assertThat(memberDto.getEmail()).isEqualTo(email);
        assertThat(memberDto.getPassword()).isEqualTo(password);
    }

    @DisplayName("존재하는 이메일로 멤버를 찾을 수 없다.")
    @Test
    void findByEmailWithoutNonExistentEmail() {
        // given
        String nonExistentEmail = "nonExistentEmail";

        // when // then
        assertThatThrownBy(() -> memberRepositoryImpl.findByEmail(nonExistentEmail))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_ERROR);
    }

    @DisplayName("refreshToken을 저장한다.")
    @Test
    void saveRefreshToken() {
        // given
        String refreshToken = "saveRefreshToken";
        String email = "email";

        // when
        memberRepositoryImpl.saveRefreshToken(refreshToken, email, 1000);

        // then
        assertThat(redisTemplate.opsForValue().get(refreshToken)).isEqualTo(email);
    }

    @DisplayName("refreshToken으로 email을 찾아온다.")
    @Test
    void findRefreshTokenByExistEmail() {
        // given
        String refreshToken = "findRefreshTokenByExistEmail";
        String email = "email";

        memberRepositoryImpl.saveRefreshToken(refreshToken, email, 1000);

        // when
        String findEmail = memberRepositoryImpl.getEmailByRefreshToken(refreshToken);

        // then
        assertThat(findEmail).isEqualTo(email);
    }

    @DisplayName("refreshToken으로 email을 찾지 못한다.")
    @Test
    void findRefreshTokenByNonExistentEmail() {
        // given
        String refreshToken = "findRefreshTokenByNonExistentEmail";

        // when // then
        assertThatThrownBy(() -> memberRepositoryImpl.getEmailByRefreshToken(refreshToken))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_REFRESH_TOKEN_ERROR);
    }

    @DisplayName("refreshToken을 삭제한다.")
    @Test
    void deleteRefreshToken() {
        // given
        String refreshToken = "deleteRefreshToken";

        redisTemplate.opsForValue().set(refreshToken, "email");

        // when
        memberRepositoryImpl.deleteRefreshToken(refreshToken);

        // then
        assertThat(redisTemplate.opsForValue().get(refreshToken)).isNull();
    }

}