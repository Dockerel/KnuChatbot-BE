package knu_chatbot.application.repository;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.infrastructure.entity.Member;

public interface MemberRepository {

    boolean existsByEmail(String email);

    void save(Member member);

    MemberDto findByEmail(String email);

    void saveRefreshToken(String refreshToken, String email, long refreshTokenExpireSeconds);

    String getEmailByRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);

    void deleteMemberByEmail(String email);

    void updatePasswordByEmail(String email, String newEncryptedPassword);
}
