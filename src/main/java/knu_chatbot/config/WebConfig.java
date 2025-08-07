package knu_chatbot.config;

import knu_chatbot.interceptor.LoginCheckInterceptor;
import knu_chatbot.monitoring.QueryCountInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final QueryCountInterceptor queryCountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(queryCountInterceptor).addPathPatterns("/**").order(1);

        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns(
                        "/api/v1/members/me",
                        "/api/v1/histories/**"
                ).order(2);
    }
}
