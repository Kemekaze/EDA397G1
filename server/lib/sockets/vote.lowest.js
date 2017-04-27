var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

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
 *     item: wefwe23r,
 *     effort: 2
 *  }
 */

module.exports = function (socket, data, callback){

  if(data.phone_id  == null || data.auth == null)
    return callback(response.BAD_REQUEST('Invalid request'));

};
