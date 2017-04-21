var RoomHandler = module.exports = function(io) {
  var self = this;
  self.io = io;
};
var method = RoomHandler.prototype;

method.exists = function(room){
  return (this.io.sockets.adapter.rooms[room] != null);
}
//handler functions
method.get = function(socket){
  return Object.keys( this.io.sockets.adapter.sids[socket.id] );
}
method.join = function(socket, room, cb){
  if(!this.exists(room)) return cb(false);
  var current_rooms = this.get(socket);
  if(current_rooms.length > 1)
    socket.leave(current_rooms[1]);
  socket.join(room);
  handler.ev.emit(handler.ev.JOIN,room);
  return cb(true);
}
method.create = function(socket, room, cb){
  if(this.exists(room)) return cb(false);
  var current_rooms = this.get(socket);
  if(current_rooms.length > 1)
    socket.leave(current_rooms[1]);
  socket.join(room);
  handler.ev.emit(handler.ev.CREATE,room);
  return cb(true);
}
method.end = function(room, cb){
  io.sockets.clients(room).forEach(function(s){
    s.leave(room);
  });
}
method.clients = function(room){
  if(!this.exists(room)) return [];
  var clients = this.io.sockets.adapter.rooms[room].sockets;
  var rtn = [];
  for (var id in clients) {
    rtn.push(this.io.sockets.connected[id]);
  }
  return rtn;
}
method.all = function(){
  return this.io.sockets.adapter.rooms;
}
method.availableFor = function(socket, cb){
  var self = this;



}
