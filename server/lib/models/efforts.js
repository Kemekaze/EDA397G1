const mongoose = require('mongoose');

// User Schema
const EffortSchema = mongoose.Schema(
  values:{
    type: [Number],
    required: true;
  }
});

// Export the model
const Efforts = module.exports = mongoose.model('Efforts', EffortSchema);
