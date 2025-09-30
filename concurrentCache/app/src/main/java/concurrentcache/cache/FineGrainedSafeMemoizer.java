
package concurrentcache.cache;

import java.util.concurrent.ConcurrentHashMap;

import concurrentcache.computable.Computable;

/**
 * Iteration 3: Fine-Grained Safe Memoizer
 * A thread-safe implementation of a memoizer that caches computed results using fine-grained locking.
 * This implementation uses ConcurrentHashMap to allow concurrent access to the cache,
 * enabling multiple threads to compute and cache results simultaneously without blocking each other.
 * 
 * <p>This approach reduces contention compared to serializing the entire compute method,
 * allowing for better performance in high-concurrency scenarios. For example, if two threads
 * call compute(56) and compute(57) at the same time, they can proceed concurrently without
 * blocking each other, as they are working on different keys.</p>
 * 
 * <p>Note: This implementation may still lead to redundant computations if multiple threads
 * compute the same value simultaneously before it is cached. For example, if two threads
 * call compute(56) at the same time, both may find that 56 is not in the cache and both
 * will compute it, leading to duplicate work and one result overwriting the other in the cache.</p>
 *
 * @param <A> the type of the input argument
 * @param <V> the type of the computed result
 */
public class FineGrainedSafeMemoizer<A,V> implements Computable<A,V> {
    private final ConcurrentHashMap<A,V> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> computable;

    public FineGrainedSafeMemoizer(Computable<A,V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = computable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
