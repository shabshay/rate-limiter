package rateLimiter;

import java.util.Calendar;

/**
 * AcquireResult
 * Result of single acquire operation
 */
public class AcquireResult {
    private final boolean succeeded;
    private final int remainHits;
    private final Calendar expirationTime;

    public AcquireResult(boolean succeeded, int remainHits, Calendar expirationTime) {

        this.succeeded = succeeded;
        this.remainHits = remainHits;
        this.expirationTime = expirationTime;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public int getRemainHits() {
        return remainHits;
    }

    public Calendar getExpirationTime() {
        return expirationTime;
    }
}
