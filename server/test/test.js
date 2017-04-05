var assert = require("assert");


describe("MongoDB", function () {
  var indexjs = require("../lib");
  var config = require("../config/database");
  const mongoose = require("mongoose");
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

  it("getClientByPhoneId: Populated", function () {
    var Client = mongoose.model('Client');
    let client = new Client({ phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'});
    client.save();

    var user = Client.getClientById(phone_id);
    assert.equal(user.phone_id, "PHONEIDSTRING");
    assert.equal(user.github_id, "GITHUBIDSTRING")

    // Client.findOne(function (err, res) {
    //     console.log(res.phone_id);
    //
    // });

    // var query = Client.findOne({ 'phone_id': 'PHONEIDSTRING' });
    //query.select('phone_id github_id')
  })

  it.skip("getClientByPhoneId: empty phone_id", function () {})

  it.skip("getClientByPhoneId: populated", function () {})

  it.skip("addClient: empty phone_id", function () {})
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
