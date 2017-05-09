var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

/**
 * signs a user out
 * @return {Object} rtn
 *  Example:
 *  {

 *  }
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  Client.remove({phone_id: socket.phone_id},function(err){
    if(!err){
      socket.git.auth = false;
      callback(response.OK({}));
      //needed ?
      setTimeout(function(){
        socket.disconnect();
      }, 1000);
    }else{
      callback(response.SERVER_ERROR('Could not sign out'));
    }
  });
};
