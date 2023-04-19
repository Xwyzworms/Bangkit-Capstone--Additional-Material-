const express = require("express");
const router = express.Router();
const fruitController = require("../controllers/fruitController");
// Define the express
// Define the Routes
// Create the path 
// Define controller for each corresponding path

router.get("/fruit", fruitController.getFruits);
router.post("/fruit", fruitController.newFruit);
router.delete("/fruit", fruitController.deleteFruits);

router.get("/fruit/:name", fruitController.getFruitByName);
router.delete("/fruite:name", fruitController.deleteFruitByName);

router.post("/uploadFruit", fruitController.uploadFruitImage);
router.post("/MLFruit", fruitController.doSomeMLShit);

module.exports = router;
