package concurrentcache.cache;

import java.util.HashMap;
import java.util.Map;

import concurrentcache.computable.Computable;

/**
 * A basic memoization implementation that caches computed results.
 * This implementation is not thread-safe and should not be used in concurrent environments.
 * 
 * <p>This class implements a simple memoization pattern where results of expensive
 * computations are cached for future use. However, due to its unsafe nature in
 * concurrent scenarios, it may lead to:</p>
 * <ul>
 *   <li>Redundant computations when multiple threads access simultaneously</li>
 *   <li>Race conditions affecting cache consistency</li>
 *   <li>Possible overwrites of cached values</li>
 * </ul>
 *
 * @param <A> the type of the argument to the computation
 * @param <V> the type of the computed result
 *
 * @see Computable
 */
public class UnsafeMemoizer<A,V> implements Computable<A,V> {
    private final Map<A,V> cache = new HashMap<>();
    private final Computable<A,V> computable;

    public UnsafeMemoizer(Computable<A,V> computable) {
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


