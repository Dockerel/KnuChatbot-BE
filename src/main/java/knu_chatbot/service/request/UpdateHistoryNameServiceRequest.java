package knu_chatbot.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateHistoryNameServiceRequest {

    private String name;

    @Builder
    public UpdateHistoryNameServiceRequest(String name) {
        this.name = name;
    }

    public static UpdateHistoryNameServiceRequest of(String name) {
        return UpdateHistoryNameServiceRequest.builder().name(name).build();
    }
}
