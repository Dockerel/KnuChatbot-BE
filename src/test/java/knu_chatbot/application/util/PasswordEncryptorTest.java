package knu_chatbot.application.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncryptorTest {

    private final PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

    @DisplayName("비밀번호를 암호화한다.")
    @Test
    void encryptPassword() {
        // given
        String rawPassword = "password";

        // when
        String encryptedPassword = passwordEncryptor.encryptPassword(rawPassword);

        // then
        assertThat(encryptedPassword).isNotEqualTo(rawPassword);
    }

    @DisplayName("올바른 비밀번호를 검증 시 true가 반환된다.")
    @Test
    void verifyPassword() {
        // given
        String rawPassword = "password";
        String encryptedPassword = passwordEncryptor.encryptPassword(rawPassword);

        // when
        boolean result = passwordEncryptor.verifyPassword(rawPassword, encryptedPassword);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("올바르지 않은 비밀번호를 검증 시 false가 반환된다.")
    @Test
    void verifyWrongPassword() {
        // given
        String rawPassword = "password";
        String encryptedPassword = passwordEncryptor.encryptPassword(rawPassword);

        String wrongPassword = "wrongPassword";

        // when
        boolean result = passwordEncryptor.verifyPassword(wrongPassword, encryptedPassword);

        // then
        assertThat(result).isFalse();
    }

}