package rateLimiter;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate counter
 * Holds the status of current rate
 */
public class RateCounter {
    private final int requestsLimit;
    private AtomicInteger numOfRequests;
    private Calendar expirationTime;

    public RateCounter(int requestsLimit, Calendar expirationTime) {
        this.requestsLimit = requestsLimit;
        this.numOfRequests = new AtomicInteger();
        this.expirationTime = expirationTime;
    }

    /**
     * @return True if limit has passed, false otherwise.
     */
    public boolean isPassedTheLimit() {
        return numOfRequests.get() > requestsLimit;
    }

    public void increase() {
        numOfRequests.incrementAndGet();
    }

    /**
     * @return True if counter expiration time has passed, false otherwise.
     */
    public boolean isExpired() {
        return expirationTime.before(Calendar.getInstance());
    }

    public Calendar getExpirationTime() {
        return expirationTime;
    }

    /**
     * @return Number of remaining hits to acquire, 0 if none.
     */
    public int remainHits() {
        return requestsLimit - numOfRequests.get() > 0
                ? requestsLimit - numOfRequests.get()
                : 0;
    }
}
