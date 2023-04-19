const mongoose = require("mongoose");

const FruitSchema = new mongoose.Schema({
    name : {type : String, required : true},
    image : String,
    description : String
})

const FruitDum = mongoose.model('FruitDum', FruitSchema);
module.exports = {FruitDum};