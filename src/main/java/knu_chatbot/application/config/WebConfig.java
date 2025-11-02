package knu_chatbot.application.config;

import knu_chatbot.presentation.interceptor.AuthInterceptor;
import knu_chatbot.presentation.resolver.AuthUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    private final AuthUserResolver authUserResolver;

    private final static List<String> whiteList = List.of(
            "/api/v1/members/check-email",
            "/api/v1/members/signup",
            "/api/v1/members/login",
            "/api/v1/members/reissue"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns(whiteList);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }

}
