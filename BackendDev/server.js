const express = require("express");
const dotenv = require('dotenv');
const path = require("path");
const app = express();
const mongoose = require("mongoose");
const routes = require("./routes/fruitHandler");
const fetch = require("node-fetch");
const model = require("./utils/modelHandler")
dotenv.config();



mongoose.connect(process.env.MONGODB_URI)


app.use(express.json()) // parses requests with JSON payloads ( jadi bentuk json )

app.use("/", routes)
app.use("/util", express.static("./utils"))
app.use("/uploads", express.static("/uploads"))


// NOT USED ATM ( Will be using TFJS, since i have a lot of exams )
const mainPyodide = null //main();
const moduleFruitDetection = null //prepareFruitDetectionScript();
const moduleHoaxDetection = null //prepareHoaxDetectionScript();
//
module.exports = {mainPyodide, moduleFruitDetection, moduleHoaxDetection};


const server = app.listen(process.env.PORT, () => {
    console.log("Listening to port " + process.env.PORT )
})


