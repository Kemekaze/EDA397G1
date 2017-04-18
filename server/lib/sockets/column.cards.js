var response = require('../responses');
var github = require('octonode');
var request = require('request');

/**
 * Gets the cards/issues for a column
 * @param {Number} data.column_id
 * @param {String} data.full_name
 * @return {Array} rtn
 * Example:
 *  [{
 *    body:"",
 *    card_id: 2258629,
 *    column_id: 824127,
 *    issue_id: 217224333,
 *    labels: [],
 *    number: 21,
 *    state: "open",
 *    title: "As a user i would like to leave a game so that i can take a break",
 *  }]
 */
module.exports = function (handler, socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.column_id  == null && data.full_name  == null)
    return callback(response.BAD_REQUEST('Invalid request'));

 socket.git.github.cards(data.column_id, function(c_error, c_resp, cards){
   if(c_resp.statusCode == 200 && !c_error){
     socket.git.github.issues(data.full_name,function(i_error, i_resp, issues){
       if(i_resp.statusCode == 200 && !i_error){
         var rtn = [];
         for (var c of cards){
           for (var i of issues){
              if(c.content_url == i.url){
                var labels = [];
                for (l of i.labels) {
                  labels.push({
                    id: l.id,
                    name: l.name
                  });
                }
                var obj = {
                  card_id: c.id,
                  column_id: data.column_id,
                  issue_id: i.id,
                  number: i.number,
                  title: i.title,
                  body: i.body,
                  state: i.state,
                  labels: labels
                };
                rtn.push(obj)
              }
            }
         }
         callback(response.OK(rtn));
      }else{
         callback(response.FORBIDDEN('Something went wrong'));
      }
      });
    }else{
      callback(response.FORBIDDEN('Something went wrong'));
    }
  })
}
