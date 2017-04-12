var github = require('octonode');
var mongoose = require('mongoose');
var Client = mongoose.model('Client');


module.exports = function (socket,data,callback){
  var dev = getDev();
  if(dev!= null){
    data.auth.username = dev.username;
    data.auth.password = dev.password;
  }
  var client = github.client(data.auth);
  client.me().info(function(err,client_data,headers){
    if(err){
      console.log(data)
      callback(401,{},[{
        error:'Invalid',
        message:'Invalid username/password'
      }]);
    }else{
      socket.git.auth = true;
      socket.git.github = client;
      Client.getByGithubId(client_data.id,function(err,c){
        if(c){
          c.username = data.auth.username;
          c.password = data.auth.password;
        }else{
          c = new Client({
            phone_id: data.phone_id,
            github_id: client_data.id,
            github: {
              username: data.auth.username,
              password: data.auth.password
            }
          });
        }
        c.save(function(err,new_client){
          callback(200,{
            login: client_data.login,
            avatar_url: client_data.avatar_url
          },[]);
        });
      });
    }
  });
};

function getDev(){
  var fs = require('fs');
  var path = __dirname + "/../../config/dev.js";
  console.log(path)
  if (fs.existsSync(path)) {
      return require(path);
  }
  return null;
}
