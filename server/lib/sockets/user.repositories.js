var response = require('../responses');
var github = require('octonode');
/**
 * Gets the users repositories
 * @return {Array} [{id, name, full_name, private}]
 */
module.exports = function (socket,data,callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  var client = socket.git.github;
  var d = client.me().repos(function(err, data, headers){
    var rtn = [];
    var fetch_count = 0;
    var total_count = data.length;
    for (var r in data)
      rtn.push({
        id: data[r].id,
        name: data[r].name,
        full_name: data[r].full_name,
        private: data[r].private
      });
    callback(response.OK(rtn));
  });
};
