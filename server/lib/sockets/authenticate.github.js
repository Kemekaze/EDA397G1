// handler for game.create
module.exports = function (socket,data,callback){
  var client = github.client(data);

  client.me().info(function(err,data,headers){
    if(err){
      callback(401,{},{
        error:'Invalid',
        message:'Invalid username/password'
      });
    }else{
      socket.git.auth = true;
      socket.git.github = client;
      callback(200,{
        login: data.login,
        avatar_url: data.avatar_url
      },{});
    }
  });

};
