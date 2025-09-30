package concurrentcache.computable;

public class PrimeFactorizeComputable implements Computable<Long, Long[]> {

    @Override
    public Long[] compute(Long arg) throws InterruptedException {
        return primeFactor(arg);
    }

    private Long[] primeFactor(Long number) {
        Long n = number;
        // worst case is when the number is power of 2.
        // eg: 1024 = 2^10, then we need 10 slots to store the factors
        Long[] factors = new Long[(int)(Math.log(number) / Math.log(2)) + 1]; 
        int i = 0;
        for (Long k = 2L; k * k <= n; k++) {
            while (n % k == 0) {
                factors[i++] = k;
                n /= k;
            }
        }
        if (n > 1) {
            factors[i++] = n;
        }
        Long[] result = new Long[i];
        
        // to return only the filled portion of the factors array
        System.arraycopy(factors, 0, result, 0, i);
        return result;
    }

}
