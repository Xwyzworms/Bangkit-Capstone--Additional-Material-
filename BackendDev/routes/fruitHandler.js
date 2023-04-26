const express = require("express");
const router = express.Router();
const multer = require("multer");
const fruitController = require("../controllers/fruitController");
// Define the express
// Define the Routes
// Create the path 
// Define controller for each corresponding path
const upload = multer();
router.get("/fruit",fruitController.getFruits);
router.post("/fruit", fruitController.uploadImage,fruitController.newFruit);
router.delete("/fruit", fruitController.deleteFruits);
router.post("/fruitWo", upload.none(), fruitController.addFruitWOImage)
router.get("/fruit/:name", fruitController.getFruitByName);
router.delete("/fruit/:name", fruitController.deleteFruitByName);

router.post("/MLFruit", fruitController.doSomeMLShit);



module.exports = router;
