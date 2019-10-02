package rateLimiter.InMemory;

import com.google.common.base.Preconditions;
import rateLimiter.base.BaseRateLimiterBuilder;
import rateLimiter.IRateLimiter;
import rateLimiter.IRateLimiterBuilder;

/**
 * Builder for in memory limiter
 * @param <TKey> Type of the keys for the limiter counters
 */
public class InMemoryRateLimiterBuilder<TKey> extends BaseRateLimiterBuilder<TKey> {

    private InMemoryRateLimiterBuilder() {
    }

    /**
     * Build new in memory rate limiter instance
     * @return Instance of InMemoryRateLimiter
     */
    @Override
    public IRateLimiter<TKey> build() {
        Preconditions.checkArgument(this.rateLimit > 0, "rateLimit must be positive");
        Preconditions.checkArgument(this.intervalTime > 0, "intervalTime must be positive");
        Preconditions.checkArgument(this.calendarTimeUnitCode > 0, "TimeUnit is not valid");

        return new InMemoryRateLimiter<>(this);
    }

    /**
     * newBuilder
     * @return New instance of builder
     */
    public static IRateLimiterBuilder newBuilder() {
        return new InMemoryRateLimiterBuilder();
    }
}
