#%%
import tensorflow as tf
import numpy as np
#%%
def getQuantizationModelInfo(modelPath : str, modelName : str):
    
    tfLiteInterpreter : tf.lite.Interpreter = tf.lite.Interpreter(model_path=modelPath)
    tfLiteInterpreter.allocate_tensors()

    input_details = tfLiteInterpreter.get_input_details()[0]
    output_details = tfLiteInterpreter.get_output_details()[0]
    dtype : np.uint8 = input_details["dtype"]
    if( dtype== np.uint8):
        inputQuantization = input_details["quantization_parameters"]
        outputQuantization = output_details["quantization_parameters"]

        inputScales = inputQuantization["scales"]
        inputZeroPoints = inputQuantization["zero_points"]

        outputScales = outputQuantization["scales"]
        outputZeroPoints = outputQuantization["zero_points"]

        inputQuants = [(f"Input Scales {x}\nInput Zeropoints {y}" ) for x,y in zip(inputScales,inputZeroPoints) ]
        outputQuants = [(f"Output Scales {x}\nOutput Zeropoints {y}" ) for x,y in zip(outputScales,outputZeroPoints) ]
        writeQuantizationModelInfo(modelName, (inputQuants, outputQuants))
    else:
        print("Emm i guess this model is not quantisized")

def writeQuantizationModelInfo(modelName : str, contents ):
    with open(modelName, "w+") as fuf:
        for x in contents:
            for lst in x:
                fuf.write(lst)
            fuf.write("\n")
if __name__ == "__main__":
    getQuantizationModelInfo("../app/src/main/ml/mobilenet_v1_1.0_224_quant.tflite", "mobilenet_v1_1.0_224_quantInfo.txt")