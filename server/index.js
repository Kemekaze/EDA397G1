const config = require("./config");
const lib = require("./lib");
const app = require('express')();
const server = require('https').createServer(config.credentials, app);
const io = require('socket.io')(server);
var socket = new lib.sockets(io,config.socketIO);
const mongoose = require("mongoose");

socket.setup();

mongoose.connect(config.database.database,function(err){
  console.log("Could not connect to: ", config.database.database);
});
mongoose.connection.on('connected', function () {
  console.log("Connected to database ", config.database.database);
});

mongoose.connection.on('error', function (err) {
  console.log("Database error: ", err);
});



var Client = mongoose.model('Client');

let client = new Client({ phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'});
client.save(function (err) {
  if (err) {
    console.log(err);
  } else {
    console.log('--> client registered');
  }
});

server.listen(config.config.PORT, function(){
  console.log("Server up, port: "+config.config.PORT);
});
