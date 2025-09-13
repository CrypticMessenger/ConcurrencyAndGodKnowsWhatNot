package ConvolutionOperator;

public class MultithreadedConvolutionOperator implements ConvolutionOperator{
    public class Worker extends Thread {
        private double[][] image;
        private double[][] kernel;
        private double[][] output;
        private int startRow;
        private int endRow;

        public Worker(double[][] image, double[][] kernel, double[][] output, int startRow, int endRow) {
            this.image = image;
            this.kernel = kernel;
            this.output = output;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            ConvolutionUtils.convolve(image, kernel, output, startRow, endRow);
        }
    }   

    @Override
    public double[][] convolution2d(double[][] padded_image, double[][] kernel, int output_height, int output_width) {
        double[][] output = new double[output_height][output_width];

        // multi-threaded way
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Using " + numThreads + " threads for convolution.");
        Worker[] workers = new Worker[numThreads];
        int rowsPerThread = output_height / numThreads;
        for (int t = 0; t < numThreads; t++) {
            int startRow = t * rowsPerThread;
            int endRow = (t == numThreads - 1) ? output_height : startRow + rowsPerThread;
            workers[t] = new Worker(padded_image, kernel, output, startRow, endRow);
            workers[t].start();
        }
        for (int t = 0; t < numThreads; t++) {
            try {
                workers[t].join();
            } catch (InterruptedException e) {              
                e.printStackTrace();
            }
        }

        return output;
    }

    
}