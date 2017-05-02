var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

/**
 * start the session
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.phone_id  == null )
    return callback(response.BAD_REQUEST('Invalid request'));
  var room = handler.room.getCurrentRoom(socket);
  if(room == null)
    return callback(response.NOT_FOUND('Session does not exist'));
  Session.findById(room,function(e,session){
    if(!e && session){
      if(session.host == socket.phone_id){
        for (var i= 0; i<session.clients_phone_id.length; i++) {
          if(session.clients_phone_id[i] == data.phone_id) session.clients_phone_id.splice(i,1);
        }
        session.save(function(e,newSession){
          if(!e){
            var clients = handler.room.clients(room);
            for (var id in clients) {
              if(clients[id].phone_id == data.phone_id){
                clients[id].leave(room);
                handler.ev.emit(handler.ev.KICKED,{
                  session:newSession,
                  socket:clients[id],
                  reason: 'You have been kicked'
                });
                break;
              }
            }
            callback(response.OK({}));
          }else{
            callback(response.SERVER_ERROR('Something went wrong'));
          }
        });

      }else {
        callback(response.FORBIDDEN('You are not the host of the game'));
      }
    }else{
      callback(response.NOT_FOUND('Session could not be found'));
    }
  });
};
