const express = require("express");
const dotenv = require('dotenv');
const app = express();
const routes = require("./routes/fruitHandler");
dotenv.config();


app.use(express.json()) // parses requests with JSON payloads ( jadi bentuk json )

app.use("/", routes)

const server = app.listen(process.env.PORT, () => {
    console.log("Listening to port " + process.env.PORT )
})


