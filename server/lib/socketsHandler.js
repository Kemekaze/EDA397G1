var Handler = module.exports = function(io,config) {
  var _this = this;
  this.timeout = config.timeout;
  this.token = config.token;
  this.clients = {};
  this.sessions = {};
  io.on('connection', function (socket) {
    _this.authenticate(socket);
    _this.gitAuthentication(socket);
    _this.on(socket);
  });
};
//client connected
Handler.prototype.clientConnected = function(socket){
  console.log('[Socket] connected: '+socket.id);
  this.clients.push(socket);
}
//client disconnected
Handler.prototype.clientDisconnected = function(socket){
  socket.on('disconnect', function (data) {
    console.log("[Socket] disconnected: ", socket.id);
    if(socket.auth)
	    clients.splice(clients.indexOf(socket),1);
  });
}
//validate token
Handler.prototype.validateToken = function(token){
    return (this.token == token);
}
//socket events for client authentication
Handler.prototype.authenticate = function(socket) {
  socket.on('authenticate', function (data) {
    if(this.validateToken(data.token)){
      socket.auth = true;
      this.clientConnected(socket);
      socket.emit("authorized");
    }else{
      socket.auth = false;
    }
  });
  setTimeout(function(){
    if (!socket.auth) {
      console.log("[Socket] unauthorized: ", socket.id);
      socket.disconnect('unauthorized');
    }
  }, this.timeout);
  this.clientDisconnected(socket);

};
//socket events for git authentication
Handler.prototype.gitAuthentication = function(socket){
  socket.on('authenticate.github', function (data) {
    var client = github.client({
      username: data.username,
      password: data.password
    });
    //other validation for when user/pass is wrong needed. This will not be null
    if(client != null){
      socket.gitClient.github= cllient;
      socket.emit('authenticate.github',this.payload(200,{},{}));
    }else{
      socket.emit('authenticate.github',this.payload(404,{},{
        error:'Invalid', 
        message:'Invalid username/password'}));
    }
    //need to handle 2factor authentication?
  });
  socket.on('authenticate.bitbucket', function (data) {

  });
}
//any other socket event
Handler.prototype.on = function(socket){
  socket.on('next.round', function (data) {

  });
}
Handler.prototype.response = function (status,payload,errors) {
  return {
    status:status,
    data: payload,
    errors: errors
  };
};