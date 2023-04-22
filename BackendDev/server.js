const express = require("express");
const dotenv = require('dotenv');
const path = require("path");
const app = express();
const mongoose = require("mongoose");
const routes = require("./routes/fruitHandler");
dotenv.config();
const {loadPyodide} = require("pyodide");
const fetch = require("node-fetch");
const https = require("https");

const imagePredictionHandlerURL  = process.env.IMAGEPREDICTION_URL
const hoaxPredictionHandlerURL =  process.env.TEXTPREDICTION_URL


async function main () {
    let pyodide = await loadPyodide();
    await pyodide.loadPackage("numpy");
    //await pyodide.loadPackage("pandas");
    await pyodide.loadPackage("pillow");

    //const responseImageDetection = await fetch(imagePredictionHandlerURL);
    //pythonScript = (pyodide.runPython(await responseImageDetection.text()))
    return pyodide
}

async function prepareFruitDetectionScript() 
{
    const responseImageDetection = await fetch(imagePredictionHandlerURL);
    const modulePy = (await mainPyodide).runPython(await responseImageDetection.text())

    return modulePy
}

async function prepareHoaxDetectionScript() 
{
    const responseHoaxPrediction = await fetch(hoaxPredictionHandlerURL);
    const modulePy = (await mainPyodide).runPython(await responseHoaxPrediction.text())

    return modulePy
}

const mainPyodide = null//main();
const moduleFruitDetection =null// prepareFruitDetectionScript();
const moduleHoaxDetection = null//prepareHoaxDetectionScript();

mongoose.connect(process.env.MONGODB_URI)


app.use(express.json()) // parses requests with JSON payloads ( jadi bentuk json )

app.use("/", routes)
app.use("/util", express.static("./utils"))
module.exports = {mainPyodide, moduleFruitDetection, moduleHoaxDetection};


const server = app.listen(process.env.PORT, () => {
    console.log("Listening to port " + process.env.PORT )
})


