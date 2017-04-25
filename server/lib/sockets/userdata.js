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
  socket.git.github.repositories(function(repo_error, repo_resp, repo_data){
    var error = false;
    var rtn = [];
    if(repo_resp.statusCode == 200 && !repo_error){
      for (var r of repo_data){
        // Get projects
        var projects = [];
        socket.git.github.projects(r.full_name,  function(project_error, project_resp, project_data){
          if(project_resp.statusCode == 200 && !project_error){
            for (var p of project_data){
              // Get columns
              var columns = [];
              socket.github.columns(p.id, function(column_error, column_resp, column_data){
                if(column_resp.statusCode == 200 && !column_error){
                  for(var c of column_data){
                    columns.push({
                      id: c.id,
                      name: c.name
                    })  
                  }
                } else {
                  error = true;
                }
              })
              projects.push({
                id: p.id,
                name: p.name,
                body: p.body,
                number: p.number,
                state: p.state,
                columns: columns
              });
            }
        } else {
          error = true;
        }
        })
        rtn.push({
          id: r.id,
          name: r.name,
          full_name: r.full_name,
          private: r.private,
          prjects: 
        });
      }
    } else {
      error = true;
    }
    if (!error){
      Console.log(rtn);
      callback(response.OK(rtn));
    } else {
      callback(response.FORBIDDEN('Something went wrong'));
    }
  });
};
