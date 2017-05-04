var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');
/**
 * Vote on lowest effort
 * @param {String} data.item_id
 * @param {String} data.effort
 * @return {Object} rtn
 *  Example: When you votes
 *  {
 *     just a 200
 *  }
 *  vote.round.result   // when all voted
 * [{
 *     effort: 2,
 *     user: {
 *    	 "login": "Kemekaze",
 *       "avatar": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *    }
 * }]
 * vote.result   // when all voted
 * {
 * effort: effort
 * item_id: item_id,
 * next_id: next_id
 * }

 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if( (data.item_id  == null || typeof data.item_id !== 'string') &&
     (data.effort == null || !Number.isInteger(data.effort)))
    return callback(response.BAD_REQUEST('Invalid request'));

    var room = handler.room.getCurrentRoom(socket);
    Session.findById(room,function(e,session){
      if(!e && session){
        if(session.state == data.item_id){
          var index = Session.findIssue(session,data.item_id);
          if(index == -1){
            return callback(response.FORBIDDEN('Item not in the session'));
          }
          var current_round = session.github.backlog_items[index].current_round;
          var round_index = Session.findRound(session, index, current_round);
          var votes = session.github.backlog_items[index].rounds[round_index].votes;
          for (var vote in votes)
            if(votes[vote].phone_id == socket.phone_id)
              return callback(response.FORBIDDEN('You have already voted this round'));
          var v = {
            phone_id: socket.phone_id,
            vote: data.effort
          };
          //session.github.backlog_items[index].rounds[round_index].votes.push(v);
          //var voted_count = session.github.backlog_items[index].rounds[round_index].votes.length;
          var obj = {};
          obj['github.backlog_items.'+index+'.rounds.'+round_index+'.votes'] = v;
          Session.findByIdAndUpdate(session._id, {$push: obj}, {new: true}, function(e,newSession){
          //session.save(function(e,newSession){
            if(!e){
              if(newSession.github.backlog_items[index].rounds[round_index].votes.length == newSession.clients_phone_id.length){
                  handler.ev.emit(handler.ev.VOTE_ROUND_RESULT,room);
              }
              callback(response.OK({}));
            }else{
              logger.error(e);
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
