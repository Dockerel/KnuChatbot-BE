package knu_chatbot.service;

import knu_chatbot.entity.History;
import knu_chatbot.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<History> getAllHistoriesByMemberId(Long memberId) {
        return historyRepository.findAllByMemberId(memberId);
    }
}
