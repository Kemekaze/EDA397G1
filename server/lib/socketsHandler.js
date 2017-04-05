var github = require('octonode');
var Handler = module.exports = function(io,config) {
  this.timeout = config.timeout;
  this.token = config.token;
  this.clients = [];
  this.sessions = [];
  this.io = io;
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
    var client = github.client({
      username: data.username,
      password: data.password
    });
    //other validation for when user/pass is wrong needed. This will not be null
    if(client != null || typeof client !== undefined){
      socket.git.github = client;
      socket.emit('authenticate.github',_this.response(200,{},{}));
    }else{
      socket.emit('authenticate.github',_this.response(404,{},{
        error:'Invalid',
        message:'Invalid username/password'}));
    }
    //need to handle 2factor authentication?
  });
  socket.on('authenticate.bitbucket', function (data) {

  });
}
//any other socket event
method.on = function(socket){
  socket.on('next.round', function (data) {

  });
}
method.response = function (status,payload,errors) {
  return {
    status:status,
    data: payload,
    errors: errors
  };
};
