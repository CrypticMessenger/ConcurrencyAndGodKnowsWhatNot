package ConvolutionOperator;

public interface ConvolutionOperator {
    public double[][] convolution2d(double[][] padded_image, double[][] kernel, int output_height, int output_width);
}