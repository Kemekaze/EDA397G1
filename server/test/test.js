var assert = require("assert");
var expect = require("chai").expect;


describe("MongoDB", function () {
  var indexjs = require("../lib");
  var config = require("../config/database");
  const mongoose = require("mongoose");
  var Client = mongoose.model('Client');

  before("Connect to database", function () {
    console.log("*** MongoDB tests ***");
  })

  beforeEach("Clean DB", function (done) {
    // clear db

    function clearDB() {
      for (var i in mongoose.connection.collections) {
        mongoose.connection.collections[i].remove(function () {})
      }
      return done();
    }

    if (mongoose.connection.readyState === 0) {
      // Mongoose promise library is depracated. Replacing it.
      mongoose.Promise = global.Promise;
      mongoose.connect(config.database, function (err) {
        if (err) {
          throw err;
        }
      });
    }
    return clearDB();
  })

  after(function (done) {
    mongoose.disconnect();
    return done();
  })

  it("getClientByPhoneId: populated", function () {
    let client = new Client({ phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'});
    client.save();
    Client.getClientByPhoneId(client.phone_id, function (err, userFromDB) {
      assert.doesNotThrow(err);
      assert.equal(userFromDB.phone_id, "PHONEIDSTRING");
      assert.equal(userFromDB.github_id, "GITHUBIDSTRING")
    });
  })

  it("test client required fields", function (done) {
    let client = new Client({ phone_id: '', github_id: ''});

    client.validate(function (err) {
      expect(err.errors.phone_id).to.exist;
      expect(err.errors.github_id).to.exist;
      done();
    });
  })

  it("addClient: normal", function () {

    Client.addClient(new Client({ phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'}));

    Client.getClientByPhoneId('PHONEIDSTRING', function (err, userFromDB) {
      assert.equal(userFromDB.phone_id, "PHONEIDSTRING");
      assert.equal(userFromDB.github_id, "GITHUBIDSTRING")
    });
  })

})
