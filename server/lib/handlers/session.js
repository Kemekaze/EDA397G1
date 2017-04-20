var events = require('events');

var SessionHandler = module.exports = function() {
  var self = this;
  self.sessions = [];

};
var method = SessionHandler.prototype;

method.newSession = function(session_id){
  this.sessions[session_id] = {
    clients: [],
    current_card: -1,
    event_emitter: new events.EventEmitter()
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
