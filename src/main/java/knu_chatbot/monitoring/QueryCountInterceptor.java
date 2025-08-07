package knu_chatbot.monitoring;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class QueryCountInterceptor implements HandlerInterceptor {

    public static final String UNKNOWN_PATH = "UNKNOWN_PATH";

    public QueryCountInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    private final MeterRegistry meterRegistry;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("QueryCountInterceptor.preHandle");
        // 1. HTTP 메서드 추출
        String httpMethod = request.getMethod();

        // 2. BEST_MATCHING_PATTERN_ATTRIBUTE로부터 path 추출
        String bestMatchPath = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (bestMatchPath == null) {
            bestMatchPath = UNKNOWN_PATH;
        }

        // 3. ThreadLocal 에 저장
        RequestContext ctx = RequestContext.builder()
                .httpMethod(httpMethod)
                .bestMatchPath(bestMatchPath)
                .build();

        RequestContextHolder.initContext(ctx);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestContext ctx = RequestContextHolder.getContext();

        // 1. 쿼리 횟수를 MeterRegistry 에 기록
        if (ctx != null) {
            Map<QueryType, Integer> queryCountByType = ctx.getQueryCountByType();
            queryCountByType.forEach((queryType, count) -> increment(ctx, queryType, count));
        }

        // 2. ThreadLocal 해제
        RequestContextHolder.clear();
    }

    private void increment(RequestContext ctx, QueryType queryType, Integer count) {
        DistributionSummary summary = DistributionSummary.builder("app.query.per_request")
                .description("Number of SQL queries per request")
                .tag("path", ctx.getBestMatchPath())
                .tag("http_method", ctx.getHttpMethod())
                .tag("query_type", queryType.name())
                .publishPercentiles(0.5, 0.95)
                .register(meterRegistry);

        summary.record(count);
    }
}
