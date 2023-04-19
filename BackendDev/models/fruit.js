const mongoose = require("mongoose");

const FruitSchema = new mongoose.Schema({
    name : {type : String, required : true},
    image : String,
    description : String
})

const FruitDum = mongoose.Model('FruitDum', FruitSchema);
module.exports = {FruitDum};