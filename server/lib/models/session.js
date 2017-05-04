const mongoose = require('mongoose')

// User Schema
const SessionSchema = mongoose.Schema({
  host: {
    type: String,
    required: true
  },
  state : {
    type: mongoose.Schema.Types.Mixed,
    default: 0
  },
  clients_phone_id:[String],
  github:{
    repo_id:{
      type: String,
      required: true
    },
    project_id:{
      type: String,
      required: true
    },
    column_id:{
      type: String,
      required: true
    },
    full_name:{
      type: String,
      required: true
    },
    lowest_effort:{
      loweset_item: Number,
      votes: [{
           phone_id: {
             type: String,
             required: true
           },
           item_id:{
             type: String,
             required: true
           }
      }]
    },
    backlog_items:[{
        issue_id:{
          type: Number,
          required: true
        },
        card_id:{
          type: Number,
          required: true
        },
        number:{
          type: Number,
          required: true
        },
        title: String,
        body: String,
        state: String,
        business_value: Number,
        effort_value: Number,
        completed: {
          type: Boolean,
          default: false
        },
        current_round: {
          type: String,
          default: ''
        },
        rounds: [{
            votes: [{
                 phone_id: {
                   type: String,
                   required: true
                 },
                 vote:{
                   type: Number,
                   required: true
                 }
            }]
        }]
    }]
  }
})

// Export the model
const Session = module.exports = mongoose.model('Session', SessionSchema)

module.exports.createSession = function(leaderClient, repo, project, column, callback){
  let newSession = new Session({
    leader: leaderClient,
    github: {
      repo_id: repo,
      project_id: project,
      column_id: column
    }
  })
  newSession.save(callback)
}
module.exports.STATE = {
 LOBBY: 0,
 LOWEST_EFFORT: 1
}
module.exports.inSession = function(session, phone_id){
  for (var id in session.clients_phone_id) {
    if(session.clients_phone_id[id] == phone_id) return true;
  }
  return false;
}
module.exports.nextIssue = function(session){
  var items = session.github.backlog_items;
  items.sort(function (a, b) {
    if(a.completed === b.completed)
      return b.business_value-a.business_value;
    else if(a.completed)
      return 1;
    else return -1;
});
  return items[0]._id;
}
module.exports.findIssue = function(session, item_id){
  var items = session.github.backlog_items;
  for (var item in items) {
    if(items[item]._id == item_id) return item;
  }
  return -1;
}
module.exports.findRound = function(session, item, round_id){
  var rounds = session.github.backlog_items[item].rounds;
  for (var round in rounds) {
    if(rounds[round]._id == round_id) return round;
  }
  return -1;
}
