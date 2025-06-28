package knu_chatbot.repository;

import knu_chatbot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(*) FROM Member m JOIN m.histories h JOIN h.questions q WHERE m.id = :memberId")
    int countQuestionsByMemberId(@Param("memberId") Long memberId);
}
