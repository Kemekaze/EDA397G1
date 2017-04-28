var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

/**
 * Leaves the a session
 * @return {Object} rtn
 *  Example:
 *  {

 *  }
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  var room = handler.room.getCurrentRoom(socket);
  if(room == null)
    return callback(response.NOT_FOUND('Session does not exist'));
  Session.findById(room,function(e,session){
    if(!e && session){
      for (var i= 0; i<session.clients_phone_id.length; i++) {
        if(session.clients_phone_id[i] == phone_id) session.clients_phone_id.splice(i,1);
      }
      session.save(function(e,newSession){
        if(!e){
          socket.leave(room);
          callback(response.OK({}));
        }else{
          callback(response.SERVER_ERROR('Something went wrong'));
        }
      });
    }else{
      callback(response.NOT_FOUND('Session could not be found'));
    }
  });
};
