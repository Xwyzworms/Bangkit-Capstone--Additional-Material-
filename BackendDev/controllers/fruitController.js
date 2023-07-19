const {FruitDum} = require("../models/fruit");
const { mainPyodide,moduleFruitDetection, moduleHoaxDetection } = require("../server");
const multer = require("multer");
const upload = multer();
const path = require('path');
const fs = require("fs");

let storage;
let uploadImage;


function defineStorage() 
{
    storage = multer.diskStorage({
        destination : (req, file, cb)=> {
            cb(null, "./uploads")
        },
        filename : (req, file, cb) => {
            cb(null, file.originalname)
        }
    })

    uploadImage = multer({storage : storage}).single('image')
}

defineStorage();


function defineNewFruitAddHandler(req, res) 
{

    const newFruit = new FruitDum({
            name : req.body.name,
            image : req.file.path,
            description : req.body.description
    })
    newFruit.save().then(result => {
        return res.json({
            "success" : true,
            "message" : "Data has been saved"
        })
    }).catch(error => {
        return res.json( {
            "success" : false,
            "message" : error
        })
    }) 
}

function defineNewFruitAddHandlerExist() 
{
    return {
        "success" : false,
        "message" : "Data is exist"
    }
}

function defineNewFruitErrorHandler(error) 
{
    return {
        "success" : false,
        'error' : error
    }   
}

// TODO 
// FIX YOUR CODE, CLEAN IT UP
const newFruit = (req, res) => {
    FruitDum.findOne({name : req.body.name}).then(data => {
        if(data == null) 
        {        
            return defineNewFruitAddHandler(req, res);       
        }
        else 
        {
            return res.json(defineNewFruitAddHandlerExist());
        }

    }).catch(error => {
        return res.json(defineNewFruitErrorHandler(error));
    }) 
}

const getFruits = (req, res, next) => 
{
    FruitDum.find({}).then(result => {
        if(result) 
        {
            return res.json({
                "success" : true,
                "message" : "Successfully get data",
                "content" : result
            });
        }
        return res.json({
           "success" : false,
           "message" : "Failed to get the data",
           "content" : result 
        })
    }).catch(error => {
        return res.json({
            "success" : false,
            "message" : error
        })
    })
}
const deleteFruits = (req, res, next) => {
    FruitDum.deleteMany({}).then(result => {
        return res.json({
            "success" : true,
            "message" : "Success fully delete all fruits"
        })

    }).catch(error => {
        return res.json("ERROR ",  error)
    })
}

const deleteFruitByName = (req, res, next) => {
    
    const {nameParams} = req.params.name;

    FruitDum.deleteOne({name : nameParams}).then(result => {
        if(result) 
        {
           return res.json({
            "success" : true,
            "message" : `Successfully delete ${nameParams}`
           })
        }
        else 
        {
           return res.json({
            "success" : false,
            "message" : `name => ${nameParams} not found`
           })
        }
        
    }).catch( error => {
        return res.json({"success" : false,
                        "message" : error});
    })
}

const getFruitByName = (req, res, next) => {
    const nameParams = req.params.name;
    FruitDum.findOne({name : nameParams}).then(result => {
        console.log(result)
        if(!result.$isEmpty()) 
        {
            return res.json({
                "success" : true,
                "message" : "Successfully getting the data",
                "content" : result
            });
        }
        else 
        {
            return res.json({
                "success" : true,
                "message" : "Successfully getting the data",
                "content" : result
            });
        }
    }).catch( error => {

        return res.json({"success" : false,
                         "message" : error})
    })

}

const addFruitWOImage = (req, res) => {
    const nameParams = req.body.name
    
    FruitDum.findOne({name : nameParams}).then(data => {
        if(data == null) {
            return defineAddFruitWOImage(req, res);
        }
        else {
          return res.json(defineNewFruitAddHandlerExist());
        }
    }).catch(error => {
        return res.json(defineNewFruitErrorHandler(error))
    })
}
function defineAddFruitWOImage(req, res) 
{      
    const newFruit = new FruitDum({
      name : req.body.name,
      image : null,
      description : req.body.description  
    })

    newFruit.save().then(result => {
        return res.json({
            "success" : true,
            "message" : "Data has been saved"
        })
    }).catch(error =>{
        return res.json({
            "success" : false,
            "message" : error
        })
    })
}

const doSomeMLShit = (req, res, next) => {
    const filePath = req.file.path;
    if(fs.existsSync(filePath)) {
        moduleFruitDetection.then( (main) => {
            main(filePath)
        })
        return res.json({
            "success" : true,
            "message" : "File terbaca"
        })
    }
    else {
        return res.json({
            "success" : true,
            "message" : "File tidak terbaca"
        })
    }
    
}

module.exports = {newFruit, getFruits, deleteFruits,
                 deleteFruitByName,getFruitByName, 
                 uploadImage, doSomeMLShit, addFruitWOImage};