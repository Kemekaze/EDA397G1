const mongoose = require('mongoose')

// User Schema
const ClientSchema = mongoose.Schema({
  phone_id: {
    type: String,
    required: true,
    unique: true
  },
  github_id: {
    type: String,
    required: true,
    unique: true
  },
  github: {
    username: {
      type: String
    },
    password: {
      type: String
    }
  }
})

// Export the model
const Client = module.exports = mongoose.model('Client', ClientSchema)

module.exports.add = function(newClient, callback){
  newClient.save(callback)
}

module.exports.removeById = function(id, callback){
  Client.findByIdAndRemove(id, callback)
}

module.exports.getAll = function(callback){
  Client.find(callback)
}

module.exports.getById = function(id, callback){
  Client.findById(id, callback)
}
module.exports.find = function(query, callback){
  Client.findOne(query, callback);
}

module.exports.getByPhoneId = function(phoneId, callback){
  const query = {phone_id: phoneId}
  Client.findOne(query, callback)
}

module.exports.getByGithubId = function(githubId, callback){
  const query = {github_id: githubId}
  Client.findOne(query, callback)
}

module.exports.getSize = function(callback){
  Client.find().count(callback)
}

module.exports.updateByGithubId = function(githubId, update, callback){
  const query = {github_id: githubId}
  const options = {new: true}
  Client.findOneAndUpdate(query, update, options, callback)

}
