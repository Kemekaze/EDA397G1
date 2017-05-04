global.serverRoot = require("path").resolve(__dirname);

global.logger = require('./logger')(serverRoot);
global.config = require("./config");
global.lib = require("./namespace");
var app = require('express')();
var server = require('https').createServer(config.credentials, app);
var io = require('socket.io')(server);

global.handler={
  socket: new lib.handlers.socket(io,config),
  room: new lib.handlers.room(io),
  ev: new lib.handlers.event(io)
};
var mongoose = require("mongoose");


handler.socket.setup();

mongoose.connect(config.database.database,function(err){
  if(err) logger.error('[DB] Unable to connect');
});
mongoose.connection.on('connected', function () {
  logger.info('[DB] Connected');
});

mongoose.connection.on('error', function (err) {
  logger.info('[DB] error:', err);
});

app.get('/', function (req, res) {
  res.sendFile(serverRoot+'/index.html');
});
server.listen(config.config.PORT, function(){
  logger.info('[Server]',require('ip').address()+':'+config.config.PORT);
});
