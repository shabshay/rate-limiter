package rateLimiter.base;

import rateLimiter.IRateLimiterBuilder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public abstract class BaseRateLimiterBuilder<TKey> implements IRateLimiterBuilder<TKey> {
    protected int intervalTime;
    protected int rateLimit;
    protected int calendarTimeUnitCode;
    private TimeUnit timeUnit;

    @Override
    public int getIntervalTime() {
        return intervalTime;
    }

    @Override
    public IRateLimiterBuilder setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
        return this;
    }

    @Override
    public int getRateLimit() {
        return rateLimit;
    }

    @Override
    public IRateLimiterBuilder setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
        return this;
    }

    @Override
    public int getCalendarTimeUnitCode() {
        return calendarTimeUnitCode;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public IRateLimiterBuilder setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        this.calendarTimeUnitCode = convertTimeUnitToCalendarCode(timeUnit);
        return this;
    }

    private int convertTimeUnitToCalendarCode(TimeUnit timeUnit) {
        switch (timeUnit) {
            case NANOSECONDS:
                throw new IllegalArgumentException("NANOSECONDS time unit isn't supported");
            case MICROSECONDS:
                throw new IllegalArgumentException("MICROSECONDS time unit isn't supported");
            case MILLISECONDS:
                return Calendar.MILLISECOND;
            case SECONDS:
                return Calendar.SECOND;
            case MINUTES:
                return Calendar.MINUTE;
            case HOURS:
                return Calendar.HOUR;
            case DAYS:
                return Calendar.DAY_OF_YEAR;
            default:
                throw new IllegalArgumentException("Time unit isn't supported");
        }
    }
}
