const mongoose = require('mongoose');

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
    access_token: {
      type: String
    },
    refresh_token: {
      type: String
    },
    expires_in: {
      type: String // Should be Date?
    }
  }
});

// Export the model
const Client = module.exports = mongoose.model('Client', ClientSchema);

module.exports.addClient = function(newClient, callback){
  newClient.save(callback);
}

module.exports.removeClient = function(id, callback){
  Client.findByIdAndRemove(id, callback);
}

module.exports.getClientById = function(id, callback){
  User.findById(id, callback);
}

module.exports.getClientByPhoneId = function(phoneId, callback){
  const query = {phone_id: phoneId};
  Client.findOne(query, callback);
}
