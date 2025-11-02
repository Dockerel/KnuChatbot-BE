package knu_chatbot.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@Transactional
public abstract class IntegrationTestSupport {

    public static final int REDIS_PORT = 6379;

    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8");
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.8-alpine").withExposedPorts(REDIS_PORT);

    static {
        mySQLContainer.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }
}
