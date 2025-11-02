package knu_chatbot.application.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

    public final int STRENGTH = 12;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(STRENGTH);

    public String encryptPassword(String rawPassword) {
        String encryptedPassword = encoder.encode(rawPassword);
        return encryptedPassword;
    }

    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }

}
