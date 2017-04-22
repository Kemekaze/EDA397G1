var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

/**
 * Autologin, tries to auto login the user
 * @param {Number} data.phone_id
 * @return {Object} rtn
 *  Example:
 *  {
 *	 "login": "Kemekaze",
 *   "avatar_url": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *  }
 */

module.exports = function (socket, data, callback){
  if(data == null)
    return callback(response.BAD_REQUEST('Invalid request'));
  tryAutoLogin(data.phone_id,function(auth){
      //no phone_id supplied, invalid request
      if(auth == null) callback(response.BAD_REQUEST('Invalid request'));
      //there was no autofetching of the credentials
      else if(auth == false) callback(response.NOT_FOUND('No matching user'));
      // found a client
      else{
        console.log(auth);
        var client = new github({
          username: auth.github.username,
          password: auth.github.password
        });
        client.info(function(err, resp, client_data){
          if(resp.statusCode != 200 || err){
            callback(response.UNAUTHORIZED('Could not auto login'));
          }else{
            socket.git.auth = true;
            socket.git.github = client;
            socket.phone_id = data.phone_id;
            auth.github.github_id = client_data.id;
            auth.github.login = client_data.login
            auth.github.avatar = client_data.avatar_url
            auth.save(function(err,new_client){
              callback(response.OK({
                login: client_data.login,
                avatar_url: client_data.avatar_url
              }));
            });
          }
        });
      }
  });
};

function tryAutoLogin(phone_id,cb){
  if(phone_id == null) cb(null);
  Client.findOne({phone_id: phone_id}, function(err,c){
    if(c){
      if(c.auto_login){
        cb(c);
      }else{
        cb(false);
      }
    }else{
      cb(false);
    }
  });
}
