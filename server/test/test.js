var assert = require("assert");


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

  afterEach(function (done) {
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

  it("getClientByPhoneId: empty phone_id", function () {
    let client = new Client({ phone_id: '', github_id: 'GITHUBIDSTRING'});
    client.save();
    Client.getClientByPhoneId(client.phone_id, function (err, userFromDB) {
      assert.throws(err);
    })
  })

  it("getClientByPhoneId: empty github_id", function () {
    let client = new Client({ phone_id: 'PHONEIDSTRING', github_id: ''});
    client.save();
    Client.getClientByPhoneId(client.phone_id, function (err, userFromDB) {
      assert.throws(err);
    })
  })

  it("getClientByPhoneId: no client added", function () {
    // no client added
    Client.getClientByPhoneId(null, function (err, userFromDB) {
      assert.throws(err);
    })
  })

  it("addClient: normal", function () {
    var user = Client.addClient(
      { phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'});

    var user = Client.getClientByPhoneId(client.phone_id);
    assert.equal(user.phone_id, "PHONEIDSTRING");
    assert.equal(user.github_id, "GITHUBIDSTRING")
  })

  it("addClient: empty phone_id", function () {})

  it("addClient: empty github_id", function () {})



})










describe("Array", function () {
  var threeArray;

  before('ArrayInitiation',function() {
    // runs before all tests in this block
    threeArray = [1,2,3];
  });

  after(function() {
    // runs after all tests in this block
  });

  beforeEach(function() {
    // runs before each test in this block
  });

  afterEach(function() {
    // runs after each test in this block
  });

  describe("#indexOf()", function () {
    it("Should return -1 when the value is not present", function () {
      assert.equal(-1, threeArray.indexOf(4));
    });
  })
})
