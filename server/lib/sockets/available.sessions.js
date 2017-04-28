var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');
var async = require('async');


/**
 * Gets the available games for the user
 * @return {Array} rtn
 *  Example:
 *  [{
 *	 "session_id": "58fa6333b4e1c63204c6a734",
 *	 "full_name": "Kemekaze/EDA397G1",
     "host" : {
        "login": "Kemekaze"
        "avatar" : "https://avatars3.githubusercontent.com/u/5463135?v=3"
     }
 *  }]
 */


module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  socket.git.github.repositories(function(error, resp, data){
    if(resp.statusCode == 200 && !error){
      var repos = [];
      for (var r of data){
          repos.push(r.full_name);
      }
      var room_ids = Object.keys(handler.room.all());
      var room_object_ids = []
      for (var r of room_ids){
          if(r.length == 24){
            room_object_ids.push(mongoose.Types.ObjectId(r));
          }
      }
      if(room_object_ids.length == 0 ) return callback(response.OK([]));

      Session.find({
        '_id': { $in: room_object_ids}
      },function(e,games){
        var viewable_games = [];
        for (var game of games) {
          for (var repo of repos) {
            if(game.github.full_name == repo && (game.state == Session.STATE.LOBBY || Session.inSession(game,socket.phone_id))){
              viewable_games.push({
                session_id: game._id,
                full_name: repo,
                phone_id: game.host,
                host: {}
              });
            }
          }
        }
        var clients_to_fetch = viewable_games.length;
        if(clients_to_fetch == 0 ) return callback(response.OK([]));
        for (var i = 0; i < clients_to_fetch; i++) {
          (function(i) {
            Client.findOne({phone_id: viewable_games[i].phone_id}, function(err,c){
              clients_to_fetch-=1;
              delete viewable_games[i].phone_id;
              viewable_games[i].host.login = c.github.login;
              viewable_games[i].host.avatar = c.github.avatar;
              if(clients_to_fetch <= 0){
                callback(response.OK(viewable_games));
              }
            });
          })(i);
        }
      });
    }else{
      callback(response.FORBIDDEN('Something went wrong'));
    }
  });
}
