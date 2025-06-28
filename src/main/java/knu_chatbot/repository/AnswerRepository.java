package knu_chatbot.repository;

import knu_chatbot.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(
            "select a from Answer a " +
                    "left join fetch a.images " +
                    "where a in :answers"
    )
    List<Answer> findAllAnswersWithImages(@Param("answers") List<Answer> answers);
}
