var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

/**
 * start the game
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));

  var room = handler.room.getCurrentRoom();
  if(room == null)
    return callback(response.NOT_FOUND('Session does not exist'));
  Session.findById(room,function(e,session){
    if(!e && session){
      if(session.host == socket.phone_id){
        session.started = true;
        session.save(function(e,newSession){
          if(!e){
            handler.ev.emit(handler.ev.START,room);
            callback(response.OK({}));
          }else{
            callback(response.SERVER_ERROR('Something went wrong'));
          }
        });
      }else {
        callback(response.UNAUTHORIZED('You are not the host of the game'));
      }
    }else{
      callback(response.NOT_FOUND('Session could not be found'));
    }
  });
};
