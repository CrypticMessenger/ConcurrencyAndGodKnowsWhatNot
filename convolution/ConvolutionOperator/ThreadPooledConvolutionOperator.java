package ConvolutionOperator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPooledConvolutionOperator implements ConvolutionOperator{
    @Override
    public double[][] convolution2d(double[][] padded_image, double[][] kernel, int output_height, int output_width) {
        double[][] output = new double[output_height][output_width];
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Using " + numThreads + " threads for convolution.");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int rowsPerThread = output_height / numThreads;
        for (int t = 0; t < numThreads; t++) {
            int startRow = t * rowsPerThread;
            int endRow = (t == numThreads - 1) ? output_height : startRow + rowsPerThread;
            final int sRow = startRow;
            final int eRow = endRow;
            executor.submit(() -> {
                ConvolutionUtils.convolve(padded_image, kernel, output, sRow, eRow);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
}
