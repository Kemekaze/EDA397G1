var response = require('../responses');
var github = require('octonode');
var mongoose = require('mongoose');
var Client = mongoose.model('Client');


module.exports = function (socket,data,callback){
  if(data == null)
    return callback(response.BAD_REQUEST('Invalid request'));
  tryAutoLogin(data.phone_id,function(auth){
      //no phone_id supplied, invalid request
      if(auth == null) callback(response.BAD_REQUEST('Invalid request'));
      //there was no autofetching of the credentials
      else if(auth == false) callback(response.NOT_FOUND('Could not auto login'));
      // found a client
      else{
        var client = github.client(auth);
        client.me().info(function(err,client_data,headers){
          if(err){
            console.log(err);
            callback(response.NOT_FOUND('Could not auto login'));
          }else{
            socket.git.auth = true;
            socket.git.github = client;
            callback(response.OK({
              login: client_data.login,
              avatar_url: client_data.avatar_url
            }));
          }
        });
      }
  });
};

function tryAutoLogin(phone_id,cb){
  if(phone_id == null) cb(null);
  Client.findOne({phone_id: phone_id}, function(err,c){
    if(c){
      cb({
        username: c.github.username,
        password: c.github.password
      });
    }else{
      cb(false);
    }
  });
}
