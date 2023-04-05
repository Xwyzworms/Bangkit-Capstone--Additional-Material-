#%%
import pandas as pd
import numpy as np
import os
from PIL import Image
import random
import re as regex
from typing import List, Tuple, Dict
#%%
"""
//TODO 
- Just create it DUDE
- Explore the Image classes and total images per class
- Explore the image properties
- Determine the Augmentation ( Optional )
- Determine the model architecture
- Save the training, validation , and testing information
"""
#%%
"""
    Constants 
"""

ALL_FILE_PATH : List[str] = []
TRAIN_PATH : List[str] = []
VALIDATION_PATH : List[str]  = []
TESTING_PATH : List[str] = []

CLASSES : List[str] = []
np.random.seed(10)

FRUIT_DATA_TRAIN : str = "./MachineLearningDev/data/fruitData/train/"
FRUIT_DATA_TEST : str ="./MachineLearningDev/data/fruitData/test/"
#%%

def plotRandomImages():
    randomImages : List[str] = []
    for className in CLASSES:
        ## DO regex For Filename ? No 
        ## Just get the path for class
        randomIndx : int = np.random.randint(0, len(ALL_FILE_PATH))
        ## Need validation  
        


def readImage(imagePath : str) -> Image:
    img : Image = Image.open(imagePath)
    return img

def readThroughPath(path : str, isTrain : bool = False):
    
    for content in os.listdir(path):
        if(isTrain):
            CLASSES.append(content)
        for fileContent in os.listdir(os.path.join(path, content)):
            fullPath : str = os.path.join(path, content, fileContent)
            ALL_FILE_PATH.append(fullPath)

def splitDataset(classes : List[str],split_percentage : float,
                  pathToTrain : str ) -> Tuple[ List[Tuple[str, str]], List[Tuple[str, str]] ]:
    """
        TODO 
        1. Get all images path
         1.1 Loop through all class
         1.2 extract filename class ( Regex ) , nah do the simple Just go through the folder
         1.3 Calculate len of the folder
         1.4 Just do the math  
         1.5 Repeat for each folder
        2. Split according params for each class 
        3. return train and validation list ( No need )
    """
    train_data : List[Tuple[str, str]] = []
    validation_data : List[Tuple[str, str]] = []
    
    for className in classes:
        folderPath : str = os.listdir(os.path.join(pathToTrain, className))
        folderLen : int = len(folderPath)
        totalForTrain : int = int(split_percentage * folderLen)  # 0.8 * 10 = 8 bener gak xD
    
        train_ = folderPath[0: totalForTrain]
        validation_ = folderPath[totalForTrain:]

        train_data.append((train_, className))
        validation_data.append((validation_, className))
    
    return train_data, validation_data


def createSolutionDataframe() -> pd.DataFrame:
    """ 
        TODO
        1. For each input image , create its label
        2. return the df 
    """
    ...

#%%
if __name__ == "__main__":
    readThroughPath(FRUIT_DATA_TRAIN, True)
    train_data, validation_data = splitDataset(CLASSES, 0.8, FRUIT_DATA_TRAIN)
    print(train_data)



# %%
