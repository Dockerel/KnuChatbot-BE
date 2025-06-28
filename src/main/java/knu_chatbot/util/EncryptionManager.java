package knu_chatbot.util;

import knu_chatbot.exception.KnuChatbotException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionManager {

    public String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new KnuChatbotException("비밀번호 암호화 중 문제가 발생했습니다.", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String bytesToHex(byte[] digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
