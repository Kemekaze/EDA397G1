var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');
module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.game_id  == null )
    return callback(response.BAD_REQUEST('Invalid request'));

  handler.room.join(socket, data.game_id, function(joined){
    if(joined){
      Session.findById(data.game_id,function(e,session){
        if(!e){
          var obj = session.toObject();
          delete obj.__v;
          delete obj.leader;
          callback(response.OK(obj));
        }else{
          callback(response.NOT_FOUND('Game could not be found'));
        }
      });
    }else{
      callback(response.NOT_FOUND('Game could not be found'));
    }
  });
};
