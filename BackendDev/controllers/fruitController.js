const FruitDum = require("../models/fruit");
const multer = require("multer");
const upload = multer();

const newFruit = (req, res, next) => {

    res.json({message : "Post new fruit"})
}

const getFruits = (req, res, next) => 
{
    res.json({message : "Get all fruits"});
}

const deleteFruits = (req, res, next) => {
    res.json({message : "Delete all fruits"});
}

const deleteFruitByName = (req, res, next) => {
    res.json({message : "Gladly i delete you"} );
}

const getFruitByName = (req, res, next) => {
    res.json({message : "All right then i get the fruit"});
}

const uploadFruitImage = (req, res, next) => {
    res.json({message : "Someee shitt goess brr"});
}

const doSomeMLShit = (req, res, next) => {
    res.json({message : "ML goes brrr"});
}

module.exports = {newFruit, getFruits, deleteFruits,
                 deleteFruitByName,getFruitByName, uploadFruitImage, doSomeMLShit};