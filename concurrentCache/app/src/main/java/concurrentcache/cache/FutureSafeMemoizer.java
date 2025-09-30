package concurrentcache.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import concurrentcache.computable.Computable;

/**
 * Iteration 4: Future-Based Safe Memoizer
 * A thread-safe implementation of a memoizer that caches computed results using Future.
 * This class implements the Computable interface and provides a caching mechanism
 * to store and reuse previously computed results.
 *
 * <p>This implementation uses {@link ConcurrentHashMap} for thread-safe cache access
 * and {@link Future} to handle ongoing computations. This ensures that only one computation
 * is performed for each unique argument, even when multiple threads request the same
 * computation simultaneously.
 *
 * <p>Key features:
 * <ul>
 *   <li>Thread-safe implementation using ConcurrentHashMap
 *   <li>Prevents redundant computations for the same input
 *   <li>Handles concurrent requests efficiently
 *   <li>Ensures consistent results across threads
 * </ul>
 *
 * <p>Note: If a computation fails, the failed Future remains in the cache,
 * which may cause subsequent calls with the same argument to fail.
 *
 * @param <A> the type of the input argument
 * @param <V> the type of the computed result
 *
 * @see Computable
 * @see Future
 * @see ConcurrentHashMap
 */
public class FutureSafeMemoizer<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public FutureSafeMemoizer(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> future = cache.get(arg);
        if (future == null) {
            Callable<V> eval = new Callable<V>() {
                public V call() throws InterruptedException {
                    return computable.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            future = ft;
            cache.put(arg, ft);
            ft.run(); // call to c.compute happens here
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
