const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const lib = require("./lib");
const config = require("./config/database");
const mongoose = require("mongoose");


mongoose.connect(config.database);
mongoose.connection.on('connected', function () {
  console.log("Connected to database ", config.database);
})

mongoose.connection.on('error', function (err) {
  console.log("Database error: ", err);
})

const SERVER_PORT = 9000;
console.log(lib);
var clients = {

};

/*
business_value: fibonocci sequence
effort_value:  predefined range
*/
/*
Session: {
  clients:[{
    ref: Client
  }],
  leader: client,
  github:{
    repo_id: repo_id,
    project_id: project_id,
    column_id: column_id,
    backlog_items:[{
        item_id: id
        name: name:
        business_value: value,
        effort_value: value,
        votes: [{
            round_id: round index
            rounds: [{
                 client_id
                 vote: effort_value
            }]
        }]
    }]
}

Client:{
  phone_id: id
  github_id: id,
  github:{
    access_token: token,
    refresh_token: token
    expires_in: date
  }
}

Efforts:{
  values:[] effort_value[]
}

*/


server.listen(9000, function(){
  console.log("Server up, port 9000");
});


io.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});
