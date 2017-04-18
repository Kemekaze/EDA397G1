var response = require('../responses');
var github = require('../api/github.js');
var mongoose = require('mongoose');
var Client = mongoose.model('Client');


module.exports = function (socket,data,callback){
  if(data == null)
    return callback(response.BAD_REQUEST('Invalid request'));
  tryAutoLogin(data.phone_id,function(auth){
      //no phone_id supplied, invalid request
      if(auth == null) callback(response.BAD_REQUEST('Invalid request'));
      //there was no autofetching of the credentials
      else if(auth == false) callback(response.NOT_FOUND('No matching user'));
      // found a client
      else{
        var client = new github(auth);
        console.log(auth);
        client.info(function(err, resp, client_data){
          console.log(err);
          console.log(resp.statusCode);
          if(err){
            callback(response.UNAUTHORIZED('Could not auto login'));
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
      if(c.auto_login){
        cb({
          username: c.github.username,
          password: c.github.password
        });
      }else{
        cb(false);
      }
    }else{
      cb(false);
    }
  });
}
