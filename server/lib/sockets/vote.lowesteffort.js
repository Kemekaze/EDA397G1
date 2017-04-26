var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

/**
 * Vote on lowest effort
 * @param {Number} data.auth.password
 * @return {Object} rtn
 *  Example:
 *  {
 *
 *  }
 */

module.exports = function (socket, data, callback){

  if(data.phone_id  == null || data.auth == null)
    return callback(response.BAD_REQUEST('Invalid request'));

};
