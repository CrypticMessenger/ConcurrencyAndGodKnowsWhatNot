package concurrentcache;

import java.util.LinkedHashMap;
import java.util.Map;

import concurrentcache.benchmarker.CacheBenchmarker;
import concurrentcache.cache.ExpiringMemoizer;
import concurrentcache.cache.FineGrainedSafeMemoizer;
import concurrentcache.cache.FutureSafeMemoizer;
import concurrentcache.cache.SerializedNaiveSafeMemoizer;
import concurrentcache.cache.UnsafeMemoizer;
import concurrentcache.computable.Computable;
import concurrentcache.computable.PrimeFactorizeComputable;

public class App {

    private static final int THREAD_POOL_SIZE = 50;
    private static final Long[] TEST_DATA = new Long[200];
    static {
        // Create 100 unique numbers
        Long[] uniqueValues = new Long[100];
        for (int i = 0; i < 100; i++) {
            uniqueValues[i] = 987654321098765432L + i;
        }

        // Intersperse them to reduce simultaneous contention on a single key
        // The structure is [val1, val2, ..., val100, val1, val2, ..., val100]
        for (int i = 0; i < 200; i++) {
            TEST_DATA[i] = uniqueValues[i % 100];
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrimeFactorizeComputable baseComputable = new PrimeFactorizeComputable();

        // --- Standard Benchmarks ---
        LinkedHashMap<String, Computable<Long, Long[]>> benchmarks = new LinkedHashMap<>();
        benchmarks.put("Baseline (No Cache)", new PrimeFactorizeComputable());
        benchmarks.put("UnsafeMemoizer", new UnsafeMemoizer<>(baseComputable));
        benchmarks.put("SerializedNaiveSafeMemoizer", new SerializedNaiveSafeMemoizer<>(baseComputable));
        benchmarks.put("FineGrainedSafeMemoizer", new FineGrainedSafeMemoizer<>(baseComputable));
        benchmarks.put("FutureSafeMemoizer", new FutureSafeMemoizer<>(baseComputable));
        
        CacheBenchmarker<Long, Long[]> benchmarker = new CacheBenchmarker<>(THREAD_POOL_SIZE, TEST_DATA);
        benchmarker.run(benchmarks);

        // --- Special Benchmark for ExpiringMemoizer ---
        Computable<Long, Long[]> expiringMemoizer = new ExpiringMemoizer<>(baseComputable, 5000);
        
        // First run (populating the cache)
        LinkedHashMap<String, Computable<Long, Long[]>> expiringBenchmark1 = new LinkedHashMap<>();
        expiringBenchmark1.put("ExpiringMemoizer (TTL 5s) - First Run", expiringMemoizer);
        benchmarker.run(expiringBenchmark1);

        // Wait for cache to expire
        System.out.println("Waiting for cache to expire (6 seconds)...");
        Thread.sleep(6000);

        // Second run (after expiration)
        LinkedHashMap<String, Computable<Long, Long[]>> expiringBenchmark2 = new LinkedHashMap<>();
        expiringBenchmark2.put("ExpiringMemoizer (TTL 5s) - Second Run (after expiration)", expiringMemoizer);
        benchmarker.run(expiringBenchmark2);
    }
}