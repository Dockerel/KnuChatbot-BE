package knu_chatbot.util;

import knu_chatbot.controller.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DiscordClient {

    @Value("${discord.environment}")
    private String environment;

    @Value("${discord.webhook-url}")
    private String webhookUrl;

    @Async
    public void sendErrorMessage(ApiResponse<?> response, String errorId) {
        if (!environment.equals("prod")) {
            return;
        }

        Map<String, Object> embedData = new HashMap<>();

        embedData.put("title", "Knu ChatBot 서버 에러 발생");

        Map<String, String> field1 = new HashMap<>();
        field1.put("name", "Time");
        field1.put("value", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

        Map<String, String> field2 = new HashMap<>();
        field2.put("name", "Http Status");
        field2.put("value", response.getStatus().name());

        Map<String, String> field3 = new HashMap<>();
        field3.put("name", "Status Code");
        field3.put("value", String.valueOf(response.getStatus().value()));

        Map<String, String> field4 = new HashMap<>();
        field4.put("name", "Error Message");
        field4.put("value", response.getMessage());

        Map<String, String> field5 = new HashMap<>();
        field5.put("name", "Error ID");
        field5.put("value", errorId);

        embedData.put("fields", List.of(field1, field2, field3, field4, field5));

        Map<String, Object> payload = new HashMap<>();
        payload.put("embeds", new Object[]{embedData});

        WebClient.create().post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.error("Discord 웹훅 전송 실패", e))
                .subscribe();
    }
}
