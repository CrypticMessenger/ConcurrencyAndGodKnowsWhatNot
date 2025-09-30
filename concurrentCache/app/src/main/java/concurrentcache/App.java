package concurrentcache;

import java.util.LinkedHashMap;

import concurrentcache.benchmarker.CacheBenchmarker;
import concurrentcache.cache.FineGrainedSafeMemoizer;
import concurrentcache.cache.FutureSafeMemoizer;
import concurrentcache.cache.SerializedNaiveSafeMemoizer;
import concurrentcache.cache.UnsafeMemoizer;
import concurrentcache.computable.Computable;
import concurrentcache.computable.PrimeFactorizeComputable;

public class App {

    private static final int THREAD_POOL_SIZE = 10;
    private static final Long[] TEST_DATA = {
        123456789L, 987654321L, 123456789L, 987654321L,
        111111111L, 222222222L, 333333333L, 444444444L,
        555555555L, 666666666L, 777777777L, 888888888L,
        999999999L, 123456789L, 987654321L, 111111111L,
        222222222L, 333333333L, 444444444L, 555555555L
    };

    public static void main(String[] args) throws InterruptedException {
        PrimeFactorizeComputable baseComputable = new PrimeFactorizeComputable();

        LinkedHashMap<String, Computable<Long, Long[]>> benchmarks = new LinkedHashMap<>();
        benchmarks.put("Baseline (No Cache)", new PrimeFactorizeComputable());
        benchmarks.put("UnsafeMemoizer", new UnsafeMemoizer<>(baseComputable));
        benchmarks.put("SerializedNaiveSafeMemoizer", new SerializedNaiveSafeMemoizer<>(baseComputable));
        benchmarks.put("FineGrainedSafeMemoizer", new FineGrainedSafeMemoizer<>(baseComputable));
        benchmarks.put("FutureSafeMemoizer", new FutureSafeMemoizer<>(baseComputable));

        CacheBenchmarker<Long, Long[]> benchmarker = new CacheBenchmarker<>(THREAD_POOL_SIZE, TEST_DATA);
        benchmarker.run(benchmarks);
    }
}