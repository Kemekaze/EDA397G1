var mongoose = require('mongoose');
var path = require('path');
var Handler = module.exports = function(io,config) {
  var self = this;
  if (config.hasOwnProperty('dev')) {
    self.dev = true;
  }else {
    self.dev = false;
  }
  self.token = config.socketIO.token;
  self.clients = [];
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
  });
}

//client connected
method.clientConnected = function(socket){
  if(this.dev) console.log('[Socket] connected:',socket.id);
  socket.git ={};
  socket.git.auth = false;
  this.clients.push(socket);
}
//client disconnected
method.clientDisconnected = function(socket){
  socket.on('disconnect', function (data) {
    if(this.dev) console.log("[Socket] disconnected:", socket.id);
    if(socket.auth)
	    clients.splice(clients.indexOf(socket),1);
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
    this.clientDisconnected(socket);
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
        require(path.join(dir, file))(socket, content,function(status, data, errors){
          socket.emit(name,self.response(status, data, errors));
        });
      });
    }
  });
}
method.response = function (status,payload,errors) {
  return {
    status:status,
    data: payload,
    errors: errors
  };
};
