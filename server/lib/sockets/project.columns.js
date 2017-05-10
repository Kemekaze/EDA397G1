var response = lib.helpers.response;

/**
 * Gets the columns for a project
 * @param {Number} data.project_id
 * @return {Array} rtn
 *  Example:
 *  [{
 *	 "id": 824127,
 *	 "name": "Backlog"
 *  }]
 */
module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.project_id  == null)
    return callback(response.BAD_REQUEST('Invalid request'));

 socket.git.github.columns(data.project_id, function(error, resp, data){
   if(resp.statusCode == 200 && !error){
      var rtn = [];
      for (var p of data){
          rtn.push({
            id: p.id,
            name: p.name
          });
      }
      callback(response.OK(rtn));
    }else{
      if(error) logger.error(error);
      callback(response.FORBIDDEN('Something went wrong'));
    }

  })
}
