package rateLimiter;

import java.util.concurrent.TimeUnit;

public interface IRateLimiterBuilder<TKey> {
    IRateLimiter<TKey> build();

    int getIntervalTime();

    IRateLimiterBuilder setIntervalTime(int intervalTime);

    int getRateLimit();

    IRateLimiterBuilder setRateLimit(int rateLimit);

    TimeUnit getTimeUnit();

    IRateLimiterBuilder setTimeUnit(TimeUnit timeUnit);

    int getCalendarTimeUnitCode();
}
