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
method.RESULT = 'session.result';

method.room = function(){
  var self = this;
  self.ee.on(self.JOINED,function(data){
    logger.info('[Event]',self.JOINED);
    self.emit(self.CLIENTS,data.room);
  });
  self.ee.on(self.CLIENTS,function(room){
    logger.info('[Event]',self.CLIENTS);
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
  self.ee.on(self.CREATED,function(data){
    logger.info('[Event]',self.CREATED);
    self.emit(self.CLIENTS,data.room);
    handler.socket.io.emit(self.CREATED, self.response.OK({}));
  });
  self.ee.on(self.STARTED,function(data){
    logger.info('[Event]',self.STARTED);
    var clients = handler.room.clients(data.room);
    for (var id in clients) {
      if(clients[id].id != data.host)
        clients[id].emit(self.STARTED,self.response.OK({}));
    }
  });
  self.ee.on(self.LEFT,function(data){
    logger.info('[Event]',self.LEFT);
    self.emit(self.CLIENTS,data.room);
  });
  self.ee.on(self.KICKED,function(data){
    logger.info('[Event]',self.KICKED);
    data.socket.emit(self.KICKED,self.response.OK({
      full_name: data.session.github.full_name,
      reason: data.reason
    }));
    self.emit(self.CLIENTS,data.room);
  });
}
method.vote = function(){
  var self = this;
  self.ee.on(self.VOTE_LOWEST_RESULT,function(room){
    logger.info('[Event]',self.VOTE_LOWEST_RESULT);
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
        session.state = Session.nextIssue(session);
        session.save(function(e,newSession){
          if(!e){
            self.io.in(room).emit(self.VOTE_LOWEST_RESULT,self.response.OK({
                item_id: newSession.github.lowest_effort.lowest_item,
                effort: lowest_effort,
                next_id: newSession.state
            }));
          }
        });
      }
    });
  });
  self.ee.on(self.VOTE_ROUND_RESULT,function(room){
    logger.info('[Event]',self.VOTE_ROUND_RESULT);
    Session.findById(room,function(e,session){
      if(!e && session){
        var index = Session.findIssue(session,session.state.toString());
        if(index != -1){
          var current_round = session.github.backlog_items[index].current_round;
          var round_index = Session.findRound(session, index, current_round);
          if(round_index != -1){
            var votes = session.github.backlog_items[index].rounds[round_index].votes;
            if(votes.length == session.clients_phone_id.length){
              var same = true;
              var v_obj = votes.toObject();
              for (var v in v_obj) {
                if(v_obj[0].vote != v_obj[v].vote) same = false;
              }
              if(same){
                session.github.backlog_items[index].effort_value = votes[0].vote;
                session.github.backlog_items[index].completed = true;
                var id = session.github.backlog_items[index]._id;
                session.state = Session.nextIssue(session);
                session.save(function(e,newSession){
                  if(!e){
                    var completed = true;
                    for (var b_item in newSession.github.backlog_items) {
                      completed = completed && newSession.github.backlog_items[b_item].completed;
                    }
                    self.io.in(room).emit(self.VOTE_RESULT,self.response.OK({
                        item_id: id,
                        effort: votes[0].vote,
                        next_id: newSession.state
                    }));
                    if(completed){
                      var _session = newSession.toObject();
                      self.io.in(room).emit(self.RESULT,self.response.OK({
                          session:_session
                      }));
                      self.io.sockets.clients(room).forEach(function(s){
                          s.leave(room);
                      });

                    }
                  }
                });
              }else{
                var id = new mongoose.Types.ObjectId();
                session.github.backlog_items[index].current_round = id;
                session.github.backlog_items[index].rounds.push({
                  _id: id,
                  votes:[]
                });
                session.save(function(e,newSession){
                  if(!e){
                    var phone_ids = []
                    var phone_ids_lookup = {}
                    for (var v in votes) {
                      phone_ids.push(votes[v].phone_id);
                      phone_ids_lookup[votes[v].phone_id] = votes[v].vote;
                    }
                    Client.find({
                      'phone_id': { $in:phone_ids}
                    },function(e,client_s){
                      var d = [];
                      for (var client in client_s) {
                        d.push({
                          vote: phone_ids_lookup[client_s[client].phone_id],
                          user: {
                            login: client_s[client].github.login,
                            avatar: client_s[client].github.avatar
                          }
                        });
                      }
                      self.io.in(room).emit(self.VOTE_ROUND_RESULT,self.response.OK(d));
                    });
                  }
                });
              }
            }
          }
        }
      }
    });
  });
}
