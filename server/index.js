var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var lib = require("./lib");
var SERVER_PORT = 9000;
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

var db =

server.listen(9000, function(){
  console.log("Server up, port 9000");
});


io.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});
