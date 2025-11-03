package knu_chatbot.infrastructure.repository;

import knu_chatbot.infrastructure.entity.Member;
import knu_chatbot.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberJpaRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("회원이 존재하면 이메일로 존재 여부 확인 시 true를 반환한다.")
    @Test
    void existsByEmailWithExistEmail() {
        // given
        String email = "email";
        Member member = Member.builder()
                .email(email)
                .password("password")
                .build();

        memberJpaRepository.save(member);

        // when
        boolean result = memberJpaRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("회원이 존재하지 않으면 이메일로 존재 여부 확인 시 false를 반환한다")
    @Test
    void existsByEmailWithNonexistEmail() {
        // given
        String nonExistentEmail = "email";

        // when
        boolean result = memberJpaRepository.existsByEmail(nonExistentEmail);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("존재하는 회원의 이메일로 회원을 찾는다.")
    @Test
    void findByEmailWithExistEmail() {
        // given
        String email = "email";
        Member member = Member.builder()
                .email(email)
                .password("password")
                .build();

        memberJpaRepository.save(member);

        // when
        Optional<Member> findMember = memberJpaRepository.findByEmail(email);

        // then
        assertThat(findMember).isPresent();
    }

    @DisplayName("존재하지 않는 회원의 이메일로 회원을 찾지 못한다.")
    @Test
    void findByEmailWithNonExistentEmail() {
        // given
        String nonExistentEmail = "email";

        // when
        Optional<Member> findMember = memberJpaRepository.findByEmail(nonExistentEmail);

        // then
        assertThat(findMember).isEmpty();
    }

    @DisplayName("존재하는 이메일로 회원을 삭제한다.")
    @Test
    void deleteByEmailWithExistEmail() {
        // given
        String email = "email";
        Member member = Member.builder()
                .email(email)
                .password("password")
                .build();

        memberJpaRepository.save(member);

        // when
        memberJpaRepository.deleteByEmail(email);

        // then
        Optional<Member> findMember = memberJpaRepository.findByEmail(email);
        assertThat(findMember).isEmpty();
    }

    @DisplayName("존재하지 않는 이메일로 회원을 삭제한다.")
    @Test
    void deleteByEmailWithNonExistentEmail() {
        // given
        String nonExistentEmail = "email";

        // when
        memberJpaRepository.deleteByEmail(nonExistentEmail);

        // then
        Optional<Member> findMember = memberJpaRepository.findByEmail(nonExistentEmail);
        assertThat(findMember).isEmpty();
    }

}