package ConvolutionOperator;
// NaiveConvolutionOperator.java

public class NaiveConvolutionOperator implements ConvolutionOperator {
    @Override
    public double[][] convolution2d(double[][] padded_image, double[][] kernel, int output_height, int output_width) {
        double[][] output = new double[output_height][output_width];
        // core logic - naive way
        ConvolutionUtils.convolve(padded_image, kernel, output, 0, output_height);
        return output;
    }
}
