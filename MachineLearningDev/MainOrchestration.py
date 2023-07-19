#%%
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from datetime import timedelta
from prefect import flow,task, get_run_logger
from prefect.tasks import task_input_hash
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression

import logging
import mlflow 
from mlflow.models import infer_signature
import mlflow.sklearn
import mlflow.pytorch
import torch

logging.basicConfig(level=logging.WARN)
logger = logging.getLogger(__name__)

# %%

main_df : pd.DataFrame = pd.DataFrame()
@task(cache_key_fn = task_input_hash, cache_expiration=timedelta(minutes=10))
def getDataframe(url : str, params : dict= None ):
    try : 
        response =  pd.read_csv(url)
    except Exception as e :
        logger.exception("Unable to parse the dataFrame")
    return response

@task
def preprocessingDataset():
    ...

@task
def splitDataset(df : pd.DataFrame):
    x_train,x_val,y_train,y_val = train_test_split(df,df[:-1], test_size=0.2, random_state=1)

    return x_train,x_val,y_train,y_val
@task
def logNumpy():
    ...
@flow(retries=3, retry_delay_seconds=5)
def prepareDataFrame():
    main_df = getDataframe(main_url)
    x_train,x_val,y_train,y_val = splitDataset(main_df)
    logger = get_run_logger()
    logger.info(x_train )
    print("Don preparing")

@flow
def customModelPytorch():
    ...

@flow
def customModelTensorflow():
    ...

#%%
prepareDataFrame()