const config = require("./config");
const lib = require("./lib");
const app = require('express')();
const server = require('https').createServer(config.credentials, app);
const io = require('socket.io')(server);
var socket = new lib.sockets(io,config);
const mongoose = require("mongoose");

socket.setup();

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
