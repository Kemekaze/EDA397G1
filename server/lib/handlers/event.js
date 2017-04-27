var events = require('events');
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

var EventHandler = module.exports = function(io) {
  var self = this;
  self.io = io;
  self.ee = new events.EventEmitter();
  self.response = lib.helpers.response;
  self.room();

};
var method = EventHandler.prototype;

method.emit = function(ev,data){
  this.ee.emit(ev,data);
}
method.JOIN = 'game.join';
method.CREATE = 'game.create';
method.START = 'game.start';


method.room = function(){
  var self = this;
  self.ee.on(self.JOIN,function(room){
    var clients = handler.room.clients(room);
    var phone_ids = []
    for (var id in clients) {
      phone_ids.push(clients[id].phone_id);
    }
    Client.find({
      'phone_id': { $in:phone_ids}
    },function(e,client_s){
      var d = [];
      for (var client in client_s) {
          d.push({
            login: client_s[client].github.login,
            avatar: client_s[client].github.avatar
          });
      }
      self.io.in(room).emit('session.clients',self.response.OK(d));
    });
  });
  self.ee.on(self.CREATE,function(room){
      handler.socket.io.emit('session.created', self.reponse.OK({}));
  });
  self.ee.on(self.START,function(room,host){
    var clients = handler.room.clients(room);
    for (var id in clients) {
      if(clients[id].id != host)
        clients[id].emit('session.start',self.response.OK({}));
    }
  });
}





var ee = EventHandler.ee;
