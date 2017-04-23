require("fs").readdirSync(__dirname).forEach(function(file) {
  if(file != 'index.js'){
    var f = file.split('.');
    if(f[f.length-1] == 'js'){
      f.pop();
      module.exports[f.join('.')] = require("./" + file);
    }
  }
});
