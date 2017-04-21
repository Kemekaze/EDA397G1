var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

/**
 * Joins a game
 * @param {Number} data.game_id
 * @return {Object} rtn
 *  Example:
 *  {
 *      _id: 58fa6333b4e1c63204c6a734,
 *     github:{
 *      backlog_items: [
 *          {
 *            business_value:17,
 *            card_id: 2388608,
 *            effort_value: -1,
 *            issue_id: 219890979,
 *            number: 57,
 *            state: "open",
 *            title: "As a user i would like to automatically log in on app start so that the user dont have to reenter credentials",
 *            votes: []
 *          },
            ....
 *      ],
 *      column_id:"824127"
 *      full_name:"Kemekaze/EDA397G1"
 *      project_id:"482637"
 *      repo_id:"86072010"
 *    },
 *    host:{
 *    	 "login": "Kemekaze",
 *       "avatar_url": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *    }
 *  }
 */

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
          Client.findOne({phone_id: obj.host}, function(err,c){
            obj.host = {
              login: c.github.login,
              avatar: c.github.avatar
            }
            callback(response.OK(obj));
          });
        }else{
          callback(response.NOT_FOUND('Game could not be found'));
        }
      });
    }else{
      callback(response.NOT_FOUND('Game could not be found'));
    }
  });
};
