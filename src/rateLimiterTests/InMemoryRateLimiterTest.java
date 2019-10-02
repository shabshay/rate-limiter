package rateLimiterTests;

import org.junit.Test;
import rateLimiter.IRateLimiter;
import rateLimiter.IRateLimiterBuilder;
import rateLimiter.InMemory.InMemoryRateLimiter;
import rateLimiter.InMemory.InMemoryRateLimiterBuilder;
import rateLimiter.RateCounter;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRateLimiterTest {
    private int rateLimit = 5;
    private int intervalTime = 5;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    @Test
    public void invalidLimitTest() {
        try {
            InMemoryRateLimiterBuilder
                    .newBuilder()
                    .setRateLimit(-1)
                    .setIntervalTime(intervalTime)
                    .setTimeUnit(timeUnit)
                    .build();

            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void invalidRateTest() {
        try {
            InMemoryRateLimiterBuilder
                    .newBuilder()
                    .setRateLimit(rateLimit)
                    .setIntervalTime(-1)
                    .setTimeUnit(timeUnit)
                    .build();
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void invalidTimeUnitTest() {
        try {
            InMemoryRateLimiterBuilder
                    .newBuilder()
                    .setRateLimit(rateLimit)
                    .setIntervalTime(intervalTime)
                    .setTimeUnit(TimeUnit.NANOSECONDS)
                    .build();
            fail();
        } catch (Exception ignored) {

        }
    }

    @Test
    public void acquireSucceededTest() {
        IRateLimiter rateLimiter = InMemoryRateLimiterBuilder
                .newBuilder()
                .setRateLimit(rateLimit)
                .setIntervalTime(intervalTime)
                .setTimeUnit(timeUnit)
                .build();

        var result = rateLimiter.acquire("acquireSucceededTest");
        assertTrue(result.isSucceeded());
        assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
        assertEquals(rateLimit - 1, result.getRemainHits());
    }

    @Test
    public void acquireFailedTest() {
        final var key = "acquireFailedTest";
        IRateLimiter rateLimiter = InMemoryRateLimiterBuilder
                .newBuilder()
                .setRateLimit(rateLimit)
                .setIntervalTime(intervalTime)
                .setTimeUnit(timeUnit)
                .build();

        for (int i = 0; i < rateLimit; i++) {
            var result = rateLimiter.acquire(key);
            assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
            assertEquals(rateLimit - i - 1, result.getRemainHits());
        }
        var result = rateLimiter.acquire(key);
        assertFalse(result.isSucceeded());
        assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
        assertEquals(0, result.getRemainHits());
    }

    @Test
    public void acquireSucceededAfterExpirationTest() throws InterruptedException {
        final var key = "acquireSucceededAfterExpirationTest";
        IRateLimiter rateLimiter = InMemoryRateLimiterBuilder
                .newBuilder()
                .setRateLimit(rateLimit)
                .setIntervalTime(intervalTime)
                .setTimeUnit(timeUnit)
                .build();

        for (int i = 0; i < rateLimit; i++) {
            var result = rateLimiter.acquire(key);
            assertTrue(result.isSucceeded());
            assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
            assertEquals(rateLimit - i - 1, result.getRemainHits());
        }

        // exceed the limit and failed to acquire
        var result = rateLimiter.acquire(key);
        assertFalse(result.isSucceeded());
        assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
        assertEquals(0, result.getRemainHits());

        // wait interval frame plus buffer before next acquire
        var ms = timeUnit.toSeconds(intervalTime + 1) * 1000;
        Thread.sleep(ms);

        // acquire completed successfully
        result = rateLimiter.acquire(key);
        assertTrue(result.isSucceeded());
        assertTrue(result.getExpirationTime().after(Calendar.getInstance()));
        assertEquals(rateLimit - 1, result.getRemainHits());
    }

    @Test
    public void counterExpiredTest() throws InterruptedException {
        var limiterToTest = new LimiterImplTest(InMemoryRateLimiterBuilder.newBuilder()
                .setIntervalTime(intervalTime).setRateLimit(rateLimit).setTimeUnit(timeUnit));
        var counter = limiterToTest.getCounterToTest();

        // wait interval frame plus buffer to verify counter was expired
        var ms = timeUnit.toSeconds(intervalTime + 1) * 1000;
        Thread.sleep(ms);

        assertTrue(counter.isExpired());
    }

    class LimiterImplTest extends InMemoryRateLimiter<String> {
        private LimiterImplTest(IRateLimiterBuilder<String> builder) {
            super(builder.getRateLimit(), builder.getIntervalTime(), builder.getCalendarTimeUnitCode(), builder.getTimeUnit());
        }

        public RateCounter getCounterToTest() {
            return super.createRateCounter();
        }
    }
}