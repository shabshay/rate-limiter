package rateLimiter;

public interface IRateLimiter<TKey> {
    AcquireResult acquire(TKey key);
}
