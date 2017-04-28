var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

/**
 * Vote on lowest effort
 * @param {String} data.item_id
 * @return {Object} rtn
 *  Example: When you votes
 *  {
 *     just a 200
 *  }
 *   vote.lowest.completed   // when all voted
 *  {
 *     item_id: item_id,
 *     effort: 2,
       next_id: next_id
 *  }
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.item_id  == null || typeof data.item_id !== 'string')
    return callback(response.BAD_REQUEST('Invalid request'));

  var room = handler.room.getCurrentRoom(socket);
  Session.findById(room,function(e,session){
    if(!e && session){
      if(session.state == Session.STATE.LOWEST_EFFORT){
        var current_votes = session.github.lowest_effort.votes;
        var voted = false;
        for (var vote in current_votes) {
          if(current_votes[vote].phone_id == socket.phone_id) voted = true;
        }
        if(voted){
          return callback(response.FORBIDDEN('You have already voted'))
        }
        session.github.lowest_effort.votes.push({
          phone_id: socket.phone_id,
          item_id: data.item_id
        });
        var voted_count = session.github.lowest_effort.votes.length;
        session.save(function(e,newSession){
          if(!e){
            if(voted_count == newSession.clients_phone_id.length){
                handler.ev.emit(handler.ev.VOTE_LOWEST_COMPLETED,room);
            }
            callback(response.OK({}));
          }else{
            callback(response.SERVER_ERROR('Something went wrong'));
          }
        });
      }else {
        callback(response.FORBIDDEN('This is not the current round'));
      }
    }else{
      callback(response.NOT_FOUND('Session could not be found'));
    }
  });
};
