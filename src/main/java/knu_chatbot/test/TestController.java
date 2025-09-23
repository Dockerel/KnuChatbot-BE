package knu_chatbot.test;

import knu_chatbot.controller.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Value("${discord.webhook-url}")
    private String discordWebhookUrl;

    @GetMapping("/api/v1/test")
    public ApiResponse<Map> test() {
        Map<String, String> map = new HashMap<>();

        map.put("hello", "welcome to knu-chatbot");

        map.put("discord_webhook_url", discordWebhookUrl.substring(0, 10));

        return ApiResponse.ok(map);
    }
}
