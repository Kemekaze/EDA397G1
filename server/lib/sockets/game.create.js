var response = lib.helpers.response;
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');
module.exports = function (handler, socket, data, callback){
  if(!socket.git.auth)
    return callback(response.UNAUTHORIZED('Unauthorized'));
  if(data.column_id  == null ||
     data.repo_id  == null ||
     data.project_id  == null ||
     data.full_name  == null )
    return callback(response.BAD_REQUEST('Invalid request'));

    socket.git.github.cards(data.column_id, function(c_error, c_resp, cards){
      if(c_resp.statusCode == 200 && !c_error){
        socket.git.github.issues(data.full_name,function(i_error, i_resp, issues){
          if(i_resp.statusCode == 200 && !i_error){
            var cards = [];
            var bv = cards.length;
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
                     issue_id: i.id,
                     number: i.number,
                     title: i.title,
                     body: i.body,
                     state: i.state,
                     business_value: bv--,
                     effort_value: -1,
                     votes:[]
                   };
                   cards.push(obj)
                 }
               }
            }
            Client.findOne({phone_id: socket.phone_id},function(e,c){
              var session = new Session({
                  leader: c._id,
                  clients:[],
                  github:{
                    repo_id: data.repo_id,
                    column_id: data.column_id,
                    project_id: data.project_id,
                    full_name: data.full_name,
                    backlog_items: cards
                  }
              });
              session.save(function(e,newSession){
                socket.join(newSession._id)
              });
            });
         }else{
            callback(response.FORBIDDEN('Something went wrong'));
         }
         });
       }else{
         callback(response.FORBIDDEN('Something went wrong'));
       }
     })


};
