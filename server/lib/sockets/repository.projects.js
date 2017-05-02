var response = lib.helpers.response;

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
 socket.git.github.projects(data.full_name, function(error, resp, data){
   if(resp.statusCode == 200 && !error){
      var rtn = [];
      for (var p of data){
          rtn.push({
            id: p.id,
            name: p.name,
            body: p.body,
            number: p.number,
            state: p.state
          });
      }
      callback(response.OK(rtn));
    }else{
      callback(response.FORBIDDEN('Something went wrong'));
    }

  })
}
