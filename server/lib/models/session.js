const mongoose = require('mongoose')

// User Schema
const SessionSchema = mongoose.Schema({
  leader: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Client',
    required: true
  },
  clients:[{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Client'
  }],
  github:{
    repo_id: String,
    project_id: String,
    column_id: String,
    backlog_items:[{
        item_id:{
          type: Number,
          required: true
        },
        name: String,
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
