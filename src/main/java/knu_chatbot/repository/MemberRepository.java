package knu_chatbot.repository;

import knu_chatbot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Query("SELECT COUNT(*) FROM Member m JOIN m.histories h JOIN h.questions q WHERE m.id = :memberId")
    int countByMemberId(@Param("memberId") Long memberId);
}
