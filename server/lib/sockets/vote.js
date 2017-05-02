var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

/**
 * Vote on lowest effort
 * @param {String} data.item_id
 * @param {String} data.effort
 * @return {Object} rtn
 *  Example: When you votes
 *  {
 *     just a 200
 *  }
 *  vote.completed   // when all voted
 * {
 * votes:[{
 *     effort: 2,
 *     user: {
 *    	 "login": "Kemekaze",
 *       "avatar": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *    }
 * }],
   item_id: item_id,
 * next_id: next_id
 * }

 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.item_id  == null || data.effort == null)
    return callback(response.BAD_REQUEST('Invalid request'));



};