const { mainPyodide,moduleFruitDetection, moduleHoaxDetection } = require("../server");

console.log("Hi")
moduleFruitDetection.then((result)=> {
    result("Hi mom")
})