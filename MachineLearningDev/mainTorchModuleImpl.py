#%%
import torch.nn as nn
import torch.optim as optim

from mainTorchModule import *
class MainTorchModuleImpl(MainTorchModule):
    
    def __init__(self, customModel, trainData , validationData):
        self.model = customModel
        self.trainDataLoader = trainData
        self.validationDataLoader = validationData
        self.lossFunction = None
        self.optimizer = None
        self.runningLoss = 0
    
    def setupTorchModel(self):
        self.flossFunction = nn.CrossEntropyLoss()
        self.optimizer = optim.Adam(self.parameters(), lr=0.001, momentum =.9)

    def training(self, images, labels):
        self.optimizer.zero_grad()

        # Forward 
        outputs = self.model(images)
        loss = self.lossFunction(outputs, labels)

        #backward and optimze
        loss.backward()
        self.optimizer.step()

        self.runningLoss += loss.item()
    def validating():
        ...

    def doTraining(self):
        epochs : int  = 5
        for epoch in range(epochs):
            for images, labels in range(3):
                
