package concurrentcache.benchmarker;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import concurrentcache.computable.Computable;

public class CacheBenchmarker<A, V> {

    private final int threadPoolSize;
    private final A[] testData;

    public CacheBenchmarker(int threadPoolSize, A[] testData) {
        this.threadPoolSize = threadPoolSize;
        this.testData = testData;
    }

    public void run(LinkedHashMap<String, Computable<A, V>> benchmarks) throws InterruptedException {
        System.out.println("Starting benchmarks...");

        for (Map.Entry<String, Computable<A, V>> entry : benchmarks.entrySet()) {
            benchmark(entry.getKey(), entry.getValue());
        }

        System.out.println("Benchmarks finished.");
    }

    private void benchmark(String name, Computable<A, V> computable) throws InterruptedException {
        System.out.println("----------------------------------------");
        System.out.println("Benchmarking: " + name);

        // Measure memory usage before
        long memoryBefore = getUsedMemory();

        long startTime = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        for (final A input : testData) {
            executor.submit(() -> {
                try {
                    computable.compute(input);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.nanoTime();

        // Measure memory usage after
        long memoryAfter = getUsedMemory();

        System.out.println("Time taken: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Memory used: " + (memoryAfter - memoryBefore) / 1024 + " KB");
        System.out.println("----------------------------------------");
    }

    private long getUsedMemory() {
        Runtime.getRuntime().gc(); // Suggest garbage collection to get a more accurate reading
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}