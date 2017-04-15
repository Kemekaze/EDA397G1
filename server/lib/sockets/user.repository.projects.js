var response = require('../responses');
var github = require('octonode');
/**
 * Gets the projects for the repo
 * @param {String} data.full_name
 * @return {Array} projects
 */
module.exports = function (socket,data,callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.full_name  == null)
    return callback(response.BAD_REQUEST('Invalid request'));

  var client = socket.git.github;
  var ghrepo = client.repo(data.full_name);
  ghrepo.projects(function(err, data, headers){
    var rtn = [];
    /*for (var r in data)
      rtn.push({
        id: data[r].id,
        name: data[r].name,
        full_name: data[r].full_name,
        private: data[r].private
      });*/
    //gives errors. need to invesitgate further..
    console.log(data);
    callback(response.OK(err));
  });
};
