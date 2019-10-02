package rateLimiter.InMemory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rateLimiter.RateCounter;
import rateLimiter.AcquireResult;
import rateLimiter.base.BaseRateLimiter;

import java.util.concurrent.TimeUnit;

/** In memory cached rate limiter
 * @param <TKey> Type of the keys for the limiter counters
 */
public class InMemoryRateLimiter<TKey> extends BaseRateLimiter<TKey> {
    // marked as Beta for more than 10 versions from some reason
    @SuppressWarnings("UnstableApiUsage")
    private final LoadingCache<TKey, RateCounter> counters;

    protected InMemoryRateLimiter(int rateLimit, int intervalTime, int calendarTimeUnitCode, TimeUnit timeUnit) {
        this.rateLimit = rateLimit;
        this.intervalTime = intervalTime;
        this.calendarTimeUnitCode = calendarTimeUnitCode;

        // concurrent expiration keys dictionary
        this.counters = CacheBuilder.newBuilder().expireAfterAccess(intervalTime, timeUnit).build(new CacheLoader<>() {
            @Override
            public RateCounter load(TKey tKey) {
                return null;
            }
        });
    }

    InMemoryRateLimiter(InMemoryRateLimiterBuilder<TKey> builder) {
        this(builder.getRateLimit(), builder.getIntervalTime(), builder.getCalendarTimeUnitCode(), builder.getTimeUnit());
    }

    /**
     * Update or create new rate counter, increase counter and check if it's passed.
     * @param key The rate counter key.
     * @return False if rate has passed the limit, true otherwise.
     */
    @Override
    public AcquireResult acquire(TKey key){
        RateCounter counter = counters.getIfPresent(key);
        if (counter == null || counter.isExpired()){
            counter = createRateCounter();
        }
        counter.increase();
        counters.put(key, counter);
        return new AcquireResult(!counter.isPassedTheLimit(), counter.remainHits(), counter.getExpirationTime());
    }
}
