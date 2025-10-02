package concurrentcache.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import concurrentcache.computable.Computable;

/**
 * Iteration 5: Expiring Memoizer
 * A thread-safe implementation of a memoizer that caches computed results with expiration.
 * This implementation uses ConcurrentHashMap for thread-safe cache access and Future
 * to handle ongoing computations. Each cached entry has a time-to-live (TTL) after which
 * it is considered expired and will be recomputed upon the next request.
 * 
 * <p>This approach ensures that cached values do not become stale over time,
 * while still providing the benefits of memoization. The expiration mechanism is
 * managed through a background cleanup task that periodically removes expired entries.</p>
 * 
 * manages cache poisoning: if a computation fails, the failed Future is removed from the cache,
 * allowing subsequent calls with the same argument to retry the computation.
 */
public class ExpiringMemoizer<A, V> implements Computable<A, V> {
    // ExpiringFuture: holds a Future AND an expiration timestamp
    // Used to track when cached entries should be invalidated
    private static class ExpiringFuture<V> {
        final Future<V> future;
        final long expiresAtMillis;
        ExpiringFuture(Future<V> future, long expiresAtMillis) {
            this.future = future;
            this.expiresAtMillis = expiresAtMillis;
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expiresAtMillis;
        }
    }

    private final long ttlMillis; // time-to-live (in ms) for each entry
    private final ConcurrentHashMap<A, ExpiringFuture<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public ExpiringMemoizer(Computable<A, V> computable, long ttlMillis) {
        this.computable = computable;
        this.ttlMillis = ttlMillis;

        // For memory efficiency: scheduled expiration cleanup (background thread)
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::cleanup, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public V compute(A arg) throws InterruptedException {
        while (true) {
            ExpiringFuture<V> expFuture = cache.get(arg);

            // Remove expired futures before use
            if (expFuture != null && expFuture.isExpired()) {
                cache.remove(arg, expFuture);
                expFuture = null;
            }

            if (expFuture == null) {
                Callable<V> eval = () -> computable.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                ExpiringFuture<V> newExpFuture = new ExpiringFuture<>(ft, System.currentTimeMillis() + ttlMillis);

                ExpiringFuture<V> existing = cache.putIfAbsent(arg, newExpFuture);
                if (existing == null) {
                    ft.run();
                    expFuture = newExpFuture;
                } else {
                    expFuture = existing;
                }
            }

            try {
                return expFuture.future.get();
            } catch (CancellationException | ExecutionException e) {
                // Remove poisoned entry and try again
                cache.remove(arg, expFuture);
                if (e instanceof ExecutionException && e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                // Either retry or propagate interruption
                if (e instanceof InterruptedException)
                    throw (InterruptedException) e;
            }
        }
    }

    // Periodic cleanup for expired entries (optional)
    private void cleanup() {
        long now = System.currentTimeMillis();
        for (Map.Entry<A, ExpiringFuture<V>> entry : cache.entrySet()) {
            ExpiringFuture<V> expFuture = entry.getValue();
            if (expFuture.isExpired() || expFuture.future.isCancelled()) {
                cache.remove(entry.getKey(), expFuture);
            }
        }
    }

    // Optionally: manual cache clearing
    public void clear() {
        cache.clear();
    }
}
