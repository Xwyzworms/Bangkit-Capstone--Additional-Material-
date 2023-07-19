import numpy as np
from PIL import Image
from typing import List, Dict, Tuple
import os

### CONSTANTS ###
HEIGHT_SIZE_MODEL_FRUIT : int  = 224
WIDTH_SIZE_MODEL_FRUIT  : int  = 224
MODEL_FRUIT_NAME : str = "mobilenet_v1_1.0_224_quant.tflite"
### CONSTANTS ###

def readImage(filename : str):
    image = Image.open(filename)
    return image    

def preprocessingImage(img : Image):
    print("3.prepareModel")
    img = img.resize((HEIGHT_SIZE_MODEL_FRUIT, WIDTH_SIZE_MODEL_FRUIT))
    img /= .255 # 0 - 1 Normalizing
    return img
    
def prepareModel():
    ...



def doPrediction():
    print("4.Prediction")

def main(filename :str):
    readImage(filename)
    preprocessingImage()
    prepareModel()
    doPrediction()

main