package knu_chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnuChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnuChatbotApplication.class, args);
    }

}
