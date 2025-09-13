import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import ConvolutionOperator.ConvolutionOperator;
import ConvolutionOperator.ConvolutionUtils;

public class ConvolutionBenchmarker {

    private ConvolutionOperator operator;

    public ConvolutionBenchmarker(final ConvolutionOperator operator) {
        this.operator = operator;
    }

    public ConvolutionBenchmarker() {
        this.operator = null;
    }

    public void setOperator(final ConvolutionOperator operator) {
        this.operator = operator;
    }

    public void benchmark(double[][] image, double[][] kernel) {
        int image_height = image.length;
        int image_width = image[0].length;
        int kernel_height = kernel.length;
        int kernel_width = kernel[0].length;
        int pad_h = (kernel_height - 1) / 2;
        int pad_w = (kernel_width - 1) / 2;
        double[][] padded_image = ConvolutionUtils.padImage(image, pad_h, pad_w);
        int output_height = image_height;
        int output_width = image_width;

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage beforeHeapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long startTime = System.nanoTime();

        operator.convolution2d(padded_image, kernel, output_height, output_width);

        long endTime = System.nanoTime();
        MemoryUsage afterHeapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long memoryUsed = afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed();


        long time_taken = (endTime - startTime) / 1_000_000;

        System.out.println("\n--- Profiling ---");
        System.out.println("Time taken: " + time_taken + " ms");
        System.out.println("Memory usage increase: " + memoryUsed / 1024 + " KB");
        System.out.println("-----------------");
    }
}
