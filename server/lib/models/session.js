const mongoose = require('mongoose')

// User Schema
const SessionSchema = mongoose.Schema({
  host: {
    type: String,
    required: true
  },
  state : {
    type: Number,
    default: 0
  },
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
           issue_id:{
             type: Number,
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
        votes: [{
            round_index: Number,
            rounds: [{
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
