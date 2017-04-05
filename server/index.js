const config = require("./config");
const lib = require("./lib");
const app = require('express')();
const server = require('https').createServer(config.credentials, app);
const io = require('socket.io')(server);
const sockets = lib.sockets(io,config.socketIO)
const mongoose = require("mongoose");



mongoose.connect(config.database.database);
mongoose.connection.on('connected', function () {
  console.log("Connected to database ", config.database.database);
})

mongoose.connection.on('error', function (err) {
  console.log("Database error: ", err);
})

server.listen(config.config.PORT, function(){
  console.log("Server up, port 9000");
});
