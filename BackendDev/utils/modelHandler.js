require("@tensorflow/tfjs-node")
const tflite = require("@tensorflow/tfjs-tflite")

MODEL_PATH = "./utils/mobilenet_v1_1.0_224_quant.tflite"
let model;
const loadModel = (patName) => 
{
    model = tflite.loadTFLiteModel(pathName);
    console.log("Loading model completed")
}
loadModel(MODEL_PATH)
console.log(model);

module.exports = {model }