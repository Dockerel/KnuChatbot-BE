package knu_chatbot.infrastructure.repository;

import knu_chatbot.application.dto.MemberDto;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.repository.MemberRepository;
import knu_chatbot.infrastructure.entity.Member;
import knu_chatbot.infrastructure.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    private final MemberMapper memberMapper;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public MemberDto findByEmail(String email) {
        Member member = getOrThrowMemberByEmail(email);
        return memberMapper.convert(member);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String email, long refreshTokenExpireSeconds) {
        redisTemplate.opsForValue().set(refreshToken, email, refreshTokenExpireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getEmailByRefreshToken(String refreshToken) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(refreshToken))
                .orElseThrow(() -> new KnuChatbotException(ErrorType.USER_INVALID_REFRESH_TOKEN_ERROR));
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    @Override
    public void deleteMemberByEmail(String email) {
        memberJpaRepository.deleteByEmail(email);
    }

    @Override
    public void updatePasswordByEmail(String email, String newEncryptedPassword) {
        Member member = getOrThrowMemberByEmail(email);

        member.changePassword(newEncryptedPassword);

        memberJpaRepository.save(member);
    }

    private Member getOrThrowMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new KnuChatbotException(ErrorType.USER_INVALID_ERROR));
    }

}
