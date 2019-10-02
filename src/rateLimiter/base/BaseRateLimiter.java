package rateLimiter.base;

import rateLimiter.IRateLimiter;
import rateLimiter.RateCounter;

import java.util.Calendar;

public abstract class BaseRateLimiter<TKey> implements IRateLimiter<TKey> {
    protected int rateLimit;
    protected int intervalTime;
    protected int calendarTimeUnitCode;

    protected RateCounter createRateCounter() {
        RateCounter counter;
        counter = new RateCounter(rateLimit, getExpirationTime());
        return counter;
    }

    private Calendar getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendarTimeUnitCode, intervalTime);
        return calendar;
    }
}
