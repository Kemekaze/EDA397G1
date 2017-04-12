var github = require('octonode');
var mongoose = require('mongoose');
var Handler = module.exports = function(io,config) {
  if (config.hasOwnProperty('dev')) {
    this.dev_credentials = config.dev;
    this.dev = true;
  }else {
    this.dev = false;
  }
  this.token = config.socketIO.token;
  this.clients = [];
  this.sessions = [];
  this.io = io;

  //db
  this.db.Client = mongoose.model('Client');


};
var method = Handler.prototype;
method.setup = function(){
  var _this = this;
  this.io.on('connection', function (socket) {
    _this.authenticate(socket);
    _this.gitAuthentication(socket);
    _this.on(socket);
  });
}

//client connected
method.clientConnected = function(socket){
  console.log('[Socket] connected:',socket.id);
  socket.git ={};
  socket.git.auth = false;
  this.clients.push(socket);
}
//client disconnected
method.clientDisconnected = function(socket){
  socket.on('disconnect', function (data) {
    console.log("[Socket] disconnected:", socket.id);
    if(socket.auth)
	    clients.splice(clients.indexOf(socket),1);
  });
}
//validate token
method.validateToken = function(token){
    return (this.token == token);
}
//socket events for client authentication
method.authenticate = function(socket) {
  if(this.validateToken(socket.handshake.query.token)){
    console.log("[Socket] authorized:", socket.id);
    socket.auth = true;
    this.clientConnected(socket);
  }else{
    socket.auth = false;
    console.log("[Socket] unauthorized:", socket.id);
    socket.disconnect('unauthorized');
    this.clientDisconnected(socket);
  }
};
//socket events for git authentication
method.gitAuthentication = function(socket){
  var _this = this;
  socket.on('authenticate.github', function (data) {
    console.log("authenticate.github");
    if (_this.dev) {
      data = _this.dev_credentials;
    }
    var client = github.client(data);

    client.me().info(function(err,data,headers){
      if(err){
        socket.emit('authenticate.github',_this.response(401,{},{
          error:'Invalid',
          message:'Invalid username/password'}));
      }else{
        console.log(data);
        socket.git.auth = true;
        socket.git.github = client;


        socket.emit('authenticate.github',_this.response(200,{
          login: data.login,
          avatar_url: data.avatar_url
        },{}));
      }
    });

    //need to handle 2factor authentication?
  });
  socket.on('authenticate.bitbucket', function (data) {

  });
}
//any other socket event
method.on = function(socket){
  require("fs").readdirSync(__dirname).forEach(function(file) {
    if(file != 'index.js'){
      var f = file.split('.');
      if(f[f.length-1] == 'js')
        module.exports[f[0]] = require("./" + file);
    }
  });
  socket.on('next.round', function (data) {

  });
  socket.on('votes', function (data) {
    //db calls

  });
}
method.response = function (status,payload,errors) {
  return {
    status:status,
    data: payload,
    errors: errors
  };
};
