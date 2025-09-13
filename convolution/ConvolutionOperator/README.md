# Convolution Benchmark Results

Here are the results from running the `Convolution.java` benchmark:
```
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
```


```
Benchmarking with NaiveConvolutionOperator

--- Profiling ---
Time taken: 1173 ms
Memory usage increase: 511812 KB
-----------------
Benchmarking with MultithreadedConvolutionOperator
Using 8 threads for convolution.

--- Profiling ---
Time taken: 550 ms
Memory usage increase: 495592 KB
-----------------
Benchmarking with ThreadPooledConvolutionOperator
Using 8 threads for convolution.

--- Profiling ---
Time taken: 513 ms
Memory usage increase: 506692 KB
-----------------
```
