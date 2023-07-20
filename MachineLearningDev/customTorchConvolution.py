#%%
import torch.nn as nn
import torch.optim as optim

class TorchConvolution(nn.Module):
    def __init__(self):
        super().__init__()
        self.conv1 : nn.Module = nn.Conv2d(3, 32, kernel_size=(3,3), stride=1,padding = 1)
        self.conv2 : nn.Module = nn.Conv2d(32, 64, kernel_size=(3,3), stride=1,padding= 1)

        self.maxPool1 : nn.Module = nn.MaxPool2d(kernel_size=(2,2))

        self.reluActivation : nn.Module = nn.ReLU()
        self.leakyActivation : nn.Module = nn.LeakyReLU()
        self.softmax : nn.Module = nn.Softmax()

        self.dropOut1 : nn.Module = nn.Dropout(0.4)
        self.dropOut2 : nn.Module = nn.Dropout(0.5)

        self.flatten : nn.Module = nn.Flatten()

        self.fullyConnectedLayer1 : nn.Module = nn.Linear(150, 128)
        self.fullyConnectedLayerFinal : nn.Module = nn.Linear(128, 33)

    def forward(self, input):
        data = self.reluActivation(self.conv1(input))
        data = self.dropOut1(data)
        
        data = self.leakyActivation(self.conv2(data))
        data = self.maxPool1(data)
        data = self.flatten(data)
        data = self.leakyActivation(self.fullyConnectedLayerFinal(data))

        return self.softmax(self.fullyConnectedLayerFinal(data))