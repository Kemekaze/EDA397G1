var response = require('../responses');
var github = require('octonode');
var request = require('request');

/**
 * Gets the projects for the repo
 * @param {String} data.full_name
 * @return {Array} rtn
 *  Example:
 *  [{
 *	 "id": 540710,
 *	 "name": "first project",
 *	 "body": "Text in the Description field",
 *	 "number": 1,
 *	 "state": "open"
 *  }]
 */
module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.full_name  == null)
    return callback(response.BAD_REQUEST('Invalid request'));

  var client = socket.git.github;
  var token = new Buffer(client.token.username + ":" + client.token.password).toString('base64');

  var options = {
    url: "https://api.github.com/repos/" + data.full_name + "/projects",
    headers: {
      'User-Agent': 'request',
      "Accept": "application/vnd.github.inertia-preview+json",
      "Authorization": "Basic " + token
    }
  }

  request(options, function(err, resp, data){
      var rtn = [];
      var projects = JSON.parse(data);
      for (var p of projects){
          rtn.push({
            id: p.id,
            name: p.name,
            body: p.body,
            number: p.number,
            state: p.state
          });
      }
    callback(response.OK(rtn));
  })
}
