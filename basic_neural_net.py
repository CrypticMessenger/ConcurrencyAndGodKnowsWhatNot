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
        m = X.shape[0]  # Number of samples
        
        # Compute gradients for the output layer
        # Loss = (1/2m) * sum((A2 - y)^2), so dL/dA2 = (A2 - y) / m
        # But since we use MSE in the loss calculation in the example,
        # dL/dA2 = 2 * (A2 - y) / m to account for the derivative of (A2 - y)^2
        dZ2 = 2 * (self.A2 - y) * sigmoid_derivative(self.Z2) / m  # (m*output)
        dW2 = np.dot(self.A1.T, dZ2)  # (hidden*m) . (m*output) = (hidden*output)
        db2 = np.sum(dZ2, axis=0)      # Sum over samples: (output)
        
        # Compute gradients for the hidden layer
        dA1 = np.dot(dZ2, self.W2.T)   # (m*output) . (output*hidden) = (m*hidden)
        dZ1 = dA1 * sigmoid_derivative(self.Z1)  # (m*hidden)
        dW1 = np.dot(X.T, dZ1)         # (input*m) . (m*hidden) = (input*hidden)
        db1 = np.sum(dZ1, axis=0)      # Sum over samples: (hidden)
        
        # Update weights and biases
        self.W2 -= learning_rate * dW2
        self.b2 -= learning_rate * db2
        self.W1 -= learning_rate * dW1
        self.b1 -= learning_rate * db1

    
# Example usage
if __name__ == "__main__":
    np.random.seed(42)
    X = np.random.rand(1, 3)  # 1 sample, 3 features
    y = np.array([[1]])       # 1 sample, 1 output

    nn = BasicNeuralNet(input_size=3, hidden_size=4, output_size=1)
    
    for epoch in range(1000):
        output = nn.forward(X)
        nn.backward(X, y, learning_rate=0.1)
        if epoch % 100 == 0:
            loss = np.mean((output - y) ** 2)
            print(f"Epoch {epoch}, Loss: {loss}")

    print("Final output after training:", nn.forward(X))