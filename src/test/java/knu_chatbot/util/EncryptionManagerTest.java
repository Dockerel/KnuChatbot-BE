package knu_chatbot.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptionManagerTest {

    private final EncryptionManager encryptionManager;

    public EncryptionManagerTest() {
        this.encryptionManager = new EncryptionManager();
    }

    @DisplayName("EncryptionManager에서 암호화시 기존의 password와 다른 문자가 나와야 한다.")
    @Test
    void encryptionTest() {
        // given
        String password = "password";

        // when
        String encryptPassword = encryptionManager.encrypt(password);

        // then
        assertThat(encryptPassword).isNotEqualTo(password);
    }
}