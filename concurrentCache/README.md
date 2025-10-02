# Concurrent Cache Memoizer

This project explores the implementation of the **Memoization** pattern in Java, with a focus on creating thread-safe and performant caches for concurrent environments. Memoization is an optimization technique used to speed up programs by storing the results of expensive function calls and returning the cached result when the same inputs occur again.

We explore several implementations, each with different approaches to handling concurrency.

## Memoizer Implementations

*   ### `UnsafeMemoizer`
    A naive, non-thread-safe implementation. It demonstrates the basic memoization pattern but will fail under concurrent load, leading to race conditions and incorrect behavior.

*   ### `SerializedNaiveSafeMemoizer`
    The simplest approach to thread safety. It uses a `synchronized` block to lock the entire `compute` method, ensuring that only one thread can access it at a time. While this guarantees correctness, it can become a major performance bottleneck in highly concurrent scenarios.

*   ### `FineGrainedSafeMemoizer`
    An implementation that uses `ConcurrentHashMap` to allow for finer-grained locking. The initial version of this memoizer demonstrates a common concurrency flaw (a check-then-act race condition), which leads to redundant computations under load. The corrected version uses the atomic `computeIfAbsent` method to resolve this.

*   ### `FutureSafeMemoizer`
    A robust implementation that uses `FutureTask` to handle the computation. When a thread requests a key that is not yet cached, a `FutureTask` is placed in the map. Other threads requesting the same key will wait on this future, ensuring the expensive computation is only ever performed once.

*   ### `ExpiringMemoizer`
    The most advanced implementation in this project. It builds on the `FutureTask` pattern but adds a time-to-live (TTL) for each cached entry. Expired entries are automatically evicted and recomputed on the next request. It also includes a background thread for periodic cleanup of expired entries from the cache.

## Benchmark Results

The following table shows the performance of each memoizer under a low-contention workload. The test involved 200 computations across 100 unique keys, run with a pool of 50 threads.

| Benchmark Name                  | Time Taken (ms) | Memory Used (KB) |
| :------------------------------ | :-------------- | :--------------- |
| Baseline (No Cache)             | 17687           | 28               |
| UnsafeMemoizer                  | 17508           | 13               |
| SerializedNaiveSafeMemoizer     | 18295           | 14               |
| FineGrainedSafeMemoizer         | 16927           | 13               |
| FutureSafeMemoizer              | 8539            | 19               |
| ExpiringMemoizer (TTL 5s) - First Run | 8489            | 19               |
| ExpiringMemoizer (TTL 5s) - Second Run| 8666            | 1                |

## How to Run

You can run the benchmark yourself using the Gradle wrapper:

```bash
./gradlew run
```
