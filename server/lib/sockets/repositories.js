var response = lib.helpers.response;
/**
 * Gets the users repositories
 * @return {Array} rtn
 *  Example:
 *  [{
 *	 "id": 540710,
 *	 "name": "EDA397G1",
 *	 "full_name": "Kemekaze/EDA397G1",
 *	 "private": true,
 *	 "state": "open"
 *  }]
 */

module.exports = function (socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  socket.git.github.repositories(function(error, resp, data){
    if(resp.statusCode == 200 && !error){
      var rtn = [];
      for (var r of data)
        rtn.push({
          id: r.id,
          name: r.name,
          full_name: r.full_name,
          private: r.private
        });
      callback(response.OK(rtn));
    }else{
      if(error) logger.error(error);
      callback(response.FORBIDDEN('Something went wrong'));
    }
  });
};
