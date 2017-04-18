var mongoose = require('mongoose');
var path = require('path');
var Handler = module.exports = function(io,config) {
  var self = this;
  self.dev = true;
  self.token = config.socketIO.token;
  self.clients = [];
  self.sessions = [];
  self.io = io;
  self.sockets = {
    on:{}
  };
};
var method = Handler.prototype;
method.setup = function(){
  var self = this;
  this.io.on('connection', function (socket) {
    self.authenticate(socket, function(){
      if(!socket.auth) return;
      self.on(socket);
    });
    self.clientDisconnected(socket);
  });
}

//client connected
method.clientConnected = function(socket){
  if(this.dev) console.log('[Socket] connected:',socket.id);
  socket.git ={};
  socket.git.auth = false;
  socket.session_id = null;
  socket.phone_id = null;
  this.clients.push(socket);
}
//client disconnected
method.clientDisconnected = function(socket){
  var self = this;
  socket.on('disconnect', function (data) {
    if(this.dev) console.log("[Socket] disconnected:", socket.id);
    if(socket.auth)
	    self.clients.splice(self.clients.indexOf(socket),1);
  });
}
//validate token
method.validateToken = function(token){
    return (this.token == token);
}
//socket events for client authentication
method.authenticate = function(socket, cb) {
  if(this.validateToken(socket.handshake.query.token)){
    if(this.dev)console.log("[Socket] authorized:", socket.id);
    socket.auth = true;
    this.clientConnected(socket);
  }else{
    socket.auth = false;
    if(this.dev) console.log("[Socket] unauthorized:", socket.id);
    socket.disconnect('unauthorized');
  }

  cb();
};
//any other socket event
method.on = function(socket){
  var self = this;
  var dir = path.join(__dirname, 'sockets');
  require("fs").readdirSync(dir).forEach(function(file) {
    var f = file.split('.');
    if(f[f.length-1] == 'js'){
      var name = "";
      for(var i = 0 ;i < f.length-1;i++){
        if(i != 0) name += '.';
        name += f[i];
      }
      socket.on(name,function(content){
        if(self.dev) console.log("[Socket]",name);
        require(path.join(dir, file))(self, socket, content,function(payload){
          socket.emit(name,payload);
        });
      });
    }
  });
}
method.newSession = function(session_id){
  this.sessions[session_id] = {
    clients: [],
    current_card: -1
  };
}
method.removeSession = function(session_id){
  if(this.sessions[session_id].clients.indexOf(socket) != -1){
    for (client of this.sessions[session_id].clients) {
      client.session_id = null;
    }
    this.sessions.splice(this.sessions.indexOf(session_id),1);
  }
}
method.addSocketToSession = function(session_id,socket){
  if(this.sessions[session_id].clients.indexOf(socket) == -1){
    this.sessions[session_id].clients.push(socket);
    //socket.session_id = session_id;
  }
}
