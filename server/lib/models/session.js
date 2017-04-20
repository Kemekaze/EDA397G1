const mongoose = require('mongoose')

// User Schema
const SessionSchema = mongoose.Schema({
  leader: {
    type: String,
    required: true
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
                 client_id: {
                   type: mongoose.Schema.Types.ObjectId,
                   ref: 'Client'
                 },
                 vote: Number
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
