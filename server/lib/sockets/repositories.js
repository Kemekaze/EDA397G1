var response = lib.helpers.response;
/**
 * Gets the users repositories
 * @return {Array} [{id, name, full_name, private}]
 */
module.exports = function (handler, socket, data, callback){
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
      callback(response.FORBIDDEN('Something went wrong'));
    }
  });
};
