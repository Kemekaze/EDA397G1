global.serverRoot = require("path").resolve(__dirname);
global.config = require("./config");
global.lib = require("./namespace");
var app = require('express')();
var server = require('https').createServer(config.credentials, app);
var io = require('socket.io')(server);

global.handler={
  socket: new lib.handlers.socket(io,config),
  session: new lib.handlers.session()
};
var mongoose = require("mongoose");


handler.socket.setup();

mongoose.connect(config.database.database,function(err){
  if(err) console.log("[DB] Unable to connect");
});
mongoose.connection.on('connected', function () {
  console.log("[DB] Connected");
});

mongoose.connection.on('error', function (err) {
  console.log("[DB] error:", err);
});

app.get('/', function (req, res) {
  res.sendFile(__dirname+'/test/index.html');
});
server.listen(config.config.PORT, function(){
  console.log("[Server]",require('ip').address()+':'+config.config.PORT);
});
