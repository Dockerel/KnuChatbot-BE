package knu_chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "validationExecutor")
    public Executor burstExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0);     // 평소에는 적은 수의 스레드 유지
        executor.setMaxPoolSize(40);     // 트래픽 급증 시 빠르게 확장
        executor.setQueueCapacity(5);    // 대기열을 작게 유지하여 빠르게 스레드 확장
        executor.setThreadNamePrefix("Burst-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  // 과부하 시 호출 스레드에서 실행
        executor.initialize();
        return executor;
    }
}
