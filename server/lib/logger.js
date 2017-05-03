module.exports = function(path){
  var winston = require('winston');
  return new (winston.Logger)({
    transports: [
      new (winston.transports.Console)(),
      new (winston.transports.File)({
        name: 'info-file',
        filename: path+'/logs/info.log',
        level: 'info'
      }),      
      new (winston.transports.File)({
        name: 'error-file',
        filename: path+'/logs/error.log',
        level: 'error'
      })
    ]
  });
}
