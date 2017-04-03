var app = require('http').createServer(handler)
var io = require('socket.io')(app);
var fs = require('fs');

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

Settings:{
  client_id: client_id,
  client_secret: secret
  redirect_uri: url,
  scope: scope[]
  state: state (random string)
}

Efforts:{
  values:[] effort_value[]
}






}*/

io.on('connection', function (socket) {



  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
});
