package knu_chatbot.repository;

import knu_chatbot.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByMemberId(Long memberId);

    @Query(
            "select h from History h " +
                    "left join fetch h.questions q " +
                    "left join fetch q.answer a " +
                    "where h.id = :historyId"
    )
    History findHistoryWithQuestionsAndAnswers(@Param("historyId") Long historyId);
}
