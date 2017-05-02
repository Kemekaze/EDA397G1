var events = require('events');
var mongoose = require('mongoose');
var Client = mongoose.model('Client');
var Session = mongoose.model('Session');

var EventHandler = module.exports = function(io) {
  var self = this;
  self.io = io;
  self.ee = new events.EventEmitter();
  self.response = lib.helpers.response;
  self.room();
  self.vote();

};
var method = EventHandler.prototype;

method.emit = function(ev,data){
  this.ee.emit(ev,data);
}
method.JOINED = 'session.joined';
method.CREATED = 'session.created';
method.STARTED = 'session.started';
method.LEFT = 'session.left';
method.KICKED = 'session.kicked';
method.CLIENTS = 'session.clients';
method.VOTE_LOWEST_RESULT = 'vote.lowest.result';
method.VOTE_ROUND_RESULT = 'vote.round.result';
method.VOTE_RESULT = 'vote.result';

method.room = function(){
  var self = this;
  self.ee.on(self.JOINED,function(room){
    console.log('[Event]',self.JOINED);
    self.emit(self.CLIENTS,room);
  });
  self.ee.on(self.CLIENTS,function(room){
    console.log('[Event]',self.CLIENTS);
    var clients = handler.room.clients(room);
    var phone_ids = []
    for (var id in clients) {
      phone_ids.push(clients[id].phone_id);
    }
    Client.find({
      'phone_id': { $in:phone_ids}
    },function(e,client_s){
      var d = [];
      for (var client in client_s) {
          d.push({
            login: client_s[client].github.login,
            avatar: client_s[client].github.avatar
          });
      }
      self.io.in(room).emit(self.CLIENTS,self.response.OK(d));
    });
  });
  self.ee.on(self.CREATED,function(room){
    console.log('[Event]',self.CREATED);
    handler.socket.io.emit(self.CREATED, self.response.OK({}));
  });
  self.ee.on(self.STARTED,function(data){
    console.log('[Event]',self.STARTED);
    var clients = handler.room.clients(data.room);
    for (var id in clients) {
      if(clients[id].id != data.host)
        clients[id].emit(self.STARTED,self.response.OK({}));
    }
  });
  self.ee.on(self.LEFT,function(data){
    console.log('[Event]',self.LEFT);
    self.emit(self.CLIENTS,data.room);
  });
  self.ee.on(self.KICKED,function(data){
    console.log('[Event]',self.KICKED);
    data.socket.emit(self.KICKED,self.response.OK({
      full_name: data.session.github.full_name,
      reason: data.reason
    }));
  });
}
method.vote = function(){
  var self = this;
  self.ee.on(self.VOTE_LOWEST_RESULT,function(room){
    console.log('[Event]',self.VOTE_LOWEST_RESULT);
    Session.findById(room,function(e,session){
      if(!e && session){
        var lowest_effort = 2;
        var current_votes = session.github.lowest_effort.votes;
        var votes ={};

        var lookup = {}
        for (var id in session.github.backlog_items) {
          lookup[session.github.backlog_items[id]._id] = {
            arr_id: id,
            item: session.github.backlog_items[id]
          }
        }
        for (var vote in current_votes) {
          var id = current_votes[vote].item_id;
          if(votes[id] == null){
            votes[id] = {
              _id: id,
              count: 1,
              b_value: lookup[id].item.business_value
            };
          }
          else{
            votes[id].count = votes[id].count+1;
          }
        }
        var sortableVotes = []
        for (var id in votes) {
          sortableVotes.push(votes[id]);
        }
        sortableVotes.sort(function (a, b) {
            return a.count - b.count || a.b_value - b.b_value;
        });
        session.github.lowest_effort.lowest_item = sortableVotes[0]._id;
        session.github.backlog_items[lookup[sortableVotes[0]._id].arr_id].effort_value = lowest_effort;
        session.github.backlog_items[lookup[sortableVotes[0]._id].arr_id].completed = true;

        session.save(function(e,newSession){
          if(!e){
            self.io.in(room).emit(self.VOTE_LOWEST_RESULT,self.response.OK({
                item_id: newSession.github.lowest_effort.lowest_item,
                effort: lowest_effort,
                next_id: Session.nextIssue(newSession)
            }));
          }
        });
      }
    });


  });


}
