package knu_chatbot.controller.response;

import knu_chatbot.entity.History;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class HistoryResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Builder
    public HistoryResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static HistoryResponse of(History history) {
        return HistoryResponse.builder()
            .id(history.getId())
            .name(history.getName())
            .createdAt(history.getCreatedAt())
            .build();
    }
}
