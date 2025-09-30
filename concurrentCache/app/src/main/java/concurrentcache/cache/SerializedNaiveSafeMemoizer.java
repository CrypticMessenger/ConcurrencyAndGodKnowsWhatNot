
package concurrentcache.cache;

import java.util.HashMap;
import java.util.Map;

import com.google.errorprone.annotations.concurrent.GuardedBy;

import concurrentcache.computable.Computable;

/**
 * Iteration 2: Serialized Naive Safe Memoizer
 * A thread-safe implementation of a memoizer that caches computed results.
 * This implementation synchronizes the entire compute method, ensuring thread safety
 * but potentially creating performance bottlenecks under high contention.
 * 
 * <p><b>Note:</b> While this implementation is thread-safe, it may lead to
 * performance issues as it serializes all access to the cache, even for different keys.</p>
 * 
 * example: if two threads call compute(56) and compute(57) at the same time, one will block the other
 * even though they are working on different keys.
 *
 * @param <A> the type of the argument to the computation
 * @param <V> the type of the computed result
 * 
 * @see Computable
 * @see HashMap
 */   
public class SerializedNaiveSafeMemoizer<A,V> implements Computable<A,V> {
    @GuardedBy("this") // documents that cache is protected by the intrinsic lock
    private final Map<A,V> cache = new HashMap<>();
    private final Computable<A,V> computable;

    public SerializedNaiveSafeMemoizer(Computable<A,V> computable) {
        this.computable = computable;
    }

    // Notice synchronized keyword here
    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = computable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }

}
