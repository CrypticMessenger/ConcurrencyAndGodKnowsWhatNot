import numpy as np

def sigmoid(x):
    return 1/ (1 + np.exp(-x))

def sigmoid_derivative(x):
    sx = sigmoid(x)
    return sx * (1 - sx)

class BasicNeuralNet:
    def __init__(self, input_size, hidden_size, output_size):
        self.W1 = np.random.randn(input_size, hidden_size) # input x hidden
        self.b1 = np.random.randn(hidden_size)
        self.W2 = np.random.randn(hidden_size, output_size) # hidden x output
        self.b2 = np.random.randn(output_size)
    
    def forward(self, X):
        self.Z1 = np.dot(X, self.W1) + self.b1 # (1*input) . (input*hidden) = (1*hidden)
        self.A1 = sigmoid(self.Z1)              # (1*hidden)
        self.Z2 = np.dot(self.A1, self.W2) + self.b2 # (1*hidden) . (hidden*output) = (1*output)
        self.A2 = sigmoid(self.Z2)              # (1*output)
        return self.A2
    
    def backward(self, X, y, learning_rate=0.01):
        return
    
basicNeuralNet = BasicNeuralNet(3, 5, 1)
output = basicNeuralNet.forward(np.array([1, 2, 3]))
print("Output of the neural network:", output)