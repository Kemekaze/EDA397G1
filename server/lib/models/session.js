const mongoose = require('mongoose');

// User Schema
const SessionSchema = mongoose.Schema({
  leader: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Client'
    //required: true  // TODO: should be required
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
});

// Export the model
const Session = module.exports = mongoose.model('Session', SessionSchema);
