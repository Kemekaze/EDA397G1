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
method.JOIN = 'session.join';
method.CREATE = 'session.create';
method.START = 'session.start';
method.VOTE_LOWEST_COMPLETED = 'vote.lowest.completed';
method.VOTE_COMPLETED = 'vote.completed';

method.room = function(){
  var self = this;
  self.ee.on(self.JOIN,function(room){
    console.log('[Event]',self.JOIN);
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
      self.io.in(room).emit('session.clients',self.response.OK(d));
    });
  });
  self.ee.on(self.CREATE,function(room){
    console.log('[Event]',self.CREATE);
    handler.socket.io.emit('session.created', self.response.OK({}));
  });
  self.ee.on(self.START,function(data){
    console.log('[Event]',self.START);
    var clients = handler.room.clients(data.room);
    for (var id in clients) {
      if(clients[id].id != data.host)
        clients[id].emit('session.start',self.response.OK({}));
    }
  });
}
method.vote = function(){
  var self = this;
  self.ee.on(self.VOTE_LOWEST_COMPLETED,function(room){
    console.log('[Event]',self.VOTE_LOWEST_COMPLETED);
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
            self.io.in(room).emit('vote.lowest.completed',self.response.OK({
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
