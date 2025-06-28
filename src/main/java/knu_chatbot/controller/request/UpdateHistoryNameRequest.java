package knu_chatbot.controller.request;

import jakarta.validation.constraints.NotBlank;
import knu_chatbot.service.request.UpdateHistoryNameServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateHistoryNameRequest {
    @NotBlank(message = "히스토리 이름은 필수입니다.")
    private String name;

    @Builder
    public UpdateHistoryNameRequest(String name) {
        this.name = name;
    }

    public static UpdateHistoryNameRequest of(String name) {
        return UpdateHistoryNameRequest.builder()
                .name(name)
                .build();
    }

    public UpdateHistoryNameServiceRequest toServiceRequest() {
        return UpdateHistoryNameServiceRequest.of(name);
    }
}
