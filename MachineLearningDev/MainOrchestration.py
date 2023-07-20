#%%
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import os

from datetime import timedelta
from prefect import flow,task, get_run_logger
from prefect.tasks import task_input_hash
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from typing import Tuple, Dict, List

import logging
import mlflow 
from mlflow.models import infer_signature
import mlflow.sklearn
import mlflow.pytorch
import torch
from torchvision import transforms
import tensorflow


logging.basicConfig(level=logging.WARN)
logger = logging.getLogger(__name__)
from importlib import reload

# Local module
import mainTorchModuleImpl
import CustomImageTorchDataset
import customTorchConvolution
# Reload Module
reload(mainTorchModuleImpl)
reload(CustomImageTorchDataset)
reload(customTorchConvolution)

from mainTorchModuleImpl import *
from CustomImageTorchDataset import *
from CustomImageTorchDataset import *

# %%
MAIN_FRUIT_TRAIN_PATH : str ="./data/fruitDataLight/train/"
MAIN_FRUIT_TEST_PATH : str ="./data/fruitDataLight/test/"

task(cache_key_fn = task_input_hash, cache_expiration=timedelta(minutes=10))
def getDataframe( pathToTrain : str = MAIN_FRUIT_TRAIN_PATH,
                  pathToTest : str = MAIN_FRUIT_TEST_PATH,
                  params : dict= None ) -> Tuple[pd.DataFrame]:
    
    trainDatadf : pd.DataFrame = pd.DataFrame(columns=["fruit_path_folder", "fruit_path_concrete", "label"])

    index : int = 0
    for fruitFolder in os.listdir(pathToTrain):
        pathToFruitFolder = os.path.join(pathToTrain,fruitFolder)
        for fruit in os.listdir(pathToFruitFolder):
            pathToConcreteFruit : str = os.path.join(pathToFruitFolder, fruit)
            newRow  : pd.Series = pd.Series( {
                                "fruit_path_folder" : pathToFruitFolder,
                                "fruit_path_concrete" :  pathToConcreteFruit,
                                "label" : fruitFolder.replace(" ", "_").lower()
                                })
            trainDatadf = pd.concat([trainDatadf, newRow.to_frame().T], ignore_index=True)
            index += 1
    return trainDatadf

@task(cache_key_fn= task_input_hash, cache_expiration=timedelta(minutes=10))
def preprocessingDataset(df : pd.DataFrame ):
    preprocessedDf = df.copy()
    preprocessedDf["label"] = df["label"].astype("category")
    preprocessedDf["label"] = preprocessedDf["label"].cat.codes

    return preprocessedDf

@task(cache_key_fn= task_input_hash, cache_expiration=timedelta(minutes=10))
def getClasses(path : str):
    classes : List[str] = []
    for i in os.listdir(path):
        i = i.replace(" ","_")
        i = i.lower()
        classes.append(i)
    return classes

@task
def splitDataset(df : pd.DataFrame):
    print(df.iloc[:,:-1])
    x_train,x_val,y_train,y_val = train_test_split(df["fruit_path_concrete"], df["label"], test_size=0.2, random_state=3)

    return x_train,x_val,y_train,y_val

@flow
def prepareTorchDataset():
    main_df = getDataframe()
    main_df = preprocessingDataset(main_df)
    x_train,x_val,y_train,y_val = splitDataset(main_df)

    logger = get_run_logger()
    logger.info(f"Information About Training Data: {x_train.shape} & Label : {y_train.shape}")
    logger.info(f"Information About Validation Data : {x_val.shape} & label : {y_val.shape}")

    trainDataset = setupTorchDatasetToLoader(makeTorchDataset(x_train, y_train))
    validationDataset = setupTorchDatasetToLoader(makeTorchDataset(x_val, y_val))
    return trainDataset, validationDataset

@task
def makeTorchDataset(x : np.ndarray, y : np.ndarray ):
    customTransform = prepareImageTransforms()

    dataset = CustomImageDataset(
        x,y,customTransform)
    
    return dataset

@task
def setupTorchDatasetToLoader(
    torchDataset : Dataset,
    batchSize : int = 32,
    shuffle : bool = True,
    drop_last : bool = True
    ):
    
    return torch.utils.data.DataLoader( torchDataset,batch_size=batchSize,
                                        shuffle = shuffle, drop_last=drop_last)

def prepareImageTransforms():
    return transforms.Compose([
        transforms.Resize((128,128)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406],
                             std=[0.229,0.224,0.224])
    ])
@task
def transformIntoTensor(x : np.ndarray, y : np.ndarray):
    return torch.tensor(x, dtype=torch.float32), torch.tensor(y, dtype=torch.float32)

def main():
    trainDataset, validationDataset = prepareTorchDataset()
    customConvolution : nn.Module = ()
    mainTorchModule : MainTorchModule = MainTorchModuleImpl()
    print(trainDataset)



#%%
main() 
    

# %%

# %%

# %%
