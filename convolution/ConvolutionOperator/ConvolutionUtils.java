package ConvolutionOperator;
public class ConvolutionUtils {
    public static void convolve(double[][] image, double[][] kernel, double[][] output, int startRow, int endRow) {
        int output_width = output[0].length;
        int kernel_height = kernel.length;
        int kernel_width = kernel[0].length;

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < output_width; j++) {
                double sum = 0.0;
                for (int ki = 0; ki < kernel_height; ki++) {
                    for (int kj = 0; kj < kernel_width; kj++) {
                        sum += image[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                output[i][j] = sum;
            }
        }
    }

     public static double[][] padImage(double[][] image, int padH, int padW) {
        int rows = image.length;
        int cols = image[0].length;
        double[][] padded = new double[rows + 2 * padH][cols + 2 * padW];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                padded[i + padH][j + padW] = image[i][j];
            }
        }
        return padded;
    }
}
