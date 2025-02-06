package knu_chatbot.repository;

import knu_chatbot.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    public List<History> findAllByMemberId(Long memberId);
}
