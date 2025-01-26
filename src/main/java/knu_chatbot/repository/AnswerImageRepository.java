package knu_chatbot.repository;

import knu_chatbot.entity.AnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerImageRepository extends JpaRepository<AnswerImage, Long> {
}
