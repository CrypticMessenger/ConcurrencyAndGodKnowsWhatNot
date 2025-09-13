import ConvolutionOperator.ConvolutionOperator;
import ConvolutionOperator.NaiveConvolutionOperator;
import ConvolutionOperator.ThreadPooledConvolutionOperator;
import ConvolutionOperator.MultithreadedConvolutionOperator;

public class Convolution {
    public static void main(String[] args) {
         // Example large image and kernel
        int length = 8000, width = 8000;
        double[][] image = new double[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                image[i][j] = i * 90 + j;
            }
        }

        double[][] kernel = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
        };

        ConvolutionOperator[] operators = {
            new NaiveConvolutionOperator(),
            new MultithreadedConvolutionOperator(),
            new ThreadPooledConvolutionOperator()
        };

        ConvolutionBenchmarker benchmarker = new ConvolutionBenchmarker();

        for (ConvolutionOperator operator : operators) {
            System.out.println("Benchmarking with " + operator.getClass().getSimpleName());
            benchmarker.setOperator(operator);
            benchmarker.benchmark(image, kernel);
        }
    }
}
