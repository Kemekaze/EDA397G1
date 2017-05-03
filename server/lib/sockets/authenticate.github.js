var response = lib.helpers.response;
var github = lib.api.github;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');

/**
 * Signs in the user
 * @param {Number} data.phone_id
 * @param {Number} data.auth.username
 * @param {Number} data.auth.password
 * @return {Object} rtn
 *  Example:
 *  {
 *	 "login": "Kemekaze",
 *   "avatar_url": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *  }
 */

module.exports = function (socket, data, callback){
  // if username or password is null and no autologin
  if(data.phone_id  == null || data.auth == null)
    return callback(response.BAD_REQUEST('Invalid request'));
  // if username or password is null and no autologin
  if((data.auth.username == null || data.auth.password == null))
    return callback(response.BAD_REQUEST('Invalid request'));
  // if username and password length is 0 and no  auto login
  if((0 === data.auth.username.length || 0 === data.auth.password.length ))
    return callback(response.UNAUTHORIZED('Invalid email or password'));

  var client = new github(data.auth);
  client.info(function(err, resp, client_data){
    if(resp.statusCode != 200 || err){
      if(err) logger.error(err);
      callback(response.UNAUTHORIZED('Invalid username or password'));
    }else{
      Client.getByGithubId(client_data.id,function(err,c){
        if(c){
          c.phone_id = data.phone_id;
          c.github.username = data.auth.username;
          c.github.password = data.auth.password;
        }else{
          c = new Client({
            phone_id: data.phone_id,
            github: {
              github_id: client_data.id,
              username: data.auth.username,
              password: data.auth.password,
              login: client_data.login,
              avatar: client_data.avatar_url
            }
          });
        }
        c.save(function(err,new_client){
          if(err){
            logger.error(err);
            callback(response.SERVER_ERROR('Something went wrong'));
          }else{
            socket.git.auth = true;
            socket.git.github = client;
            socket.phone_id = new_client.phone_id;
            callback(response.OK({
              login: client_data.login,
              avatar_url: client_data.avatar_url
            }));
          }
        });
      });
    }
  });
};
