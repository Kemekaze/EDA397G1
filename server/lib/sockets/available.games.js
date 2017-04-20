var response = lib.helpers.response;

module.exports = function (handler, socket, data, callback){

  var rtn =
    [
      {
        "session_id": "sessionid-1",
        "name": "gamename-1",
        "repo_id": "repoid-1"
      },
      {
        "session_id": "sessionid-2",
        "name": "gamename-2",
        "repo_id": "repoid-2"
      },
      {
        "session_id": "sessionid-3",
        "name": "gamename-3",
        "repo_id": "repoid-3"
      }
    ]
  callback(response.OK(rtn));
}
