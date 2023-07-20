
from abc import ABC, abstractmethod
class MainTorchModule(ABC):

    @abstractmethod
    def setupTorchModel(self):
        raise NotImplementedError("Optimizer need to be setup")

    @abstractmethod
    def setupCustomLoop(self):
        raise NotImplementedError("Custom Loop must be implemented")
    