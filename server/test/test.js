const assert = require("assert")
const expect = require('chai').expect
const indexjs = require("../lib")
const config = require("../config/database")
const mongoose = require("mongoose")
const Client = mongoose.model('Client')

describe("*** MongoDB/Mongoose tests ***", function () {

  before("Connect to database", function (done){
    // runs before all tests in this block
    mongoose.Promise = global.Promise
    mongoose.connect(config.database, function (error) {
      if (error){
        throw error // Handle failed connection
      }else{
        console.log('mongoose.connection.readyState:'+ mongoose.connection.readyState)
      }
      done()
    })
  })

  after(function(done) {
    // runs after all tests in this block
    mongoose.disconnect()
    done()
  })

  beforeEach("Clear all clients before each test", function (done) {
    // runs before each test in this block
    Client.remove({}, function(err, res){
      if(err != null){
        console.log(err)
      } else {
        //console.log("Database clients: cleared:" + res)
      }
      done()
    })
  })

  afterEach(function() {
    // runs after each test in this block
  })


  //----------------------------------------------------------
  //          Actual tests begin here.
  //----------------------------------------------------------

  it("[Setup]: Clients should be empty.", function (done) {
    Client.getSize(function (err, count) {
      assert.equal(count, 0)  //assert.equal(actual, expected)
      done()
    })
  })

  it("[Client model]: Test add.", function (done) {
    let client = new Client({
      phone_id: "PHONE-ID-1",
      github_id: "GITHUB-ID-1",
      github: {
        access_token: "ACCESS-TOKEN-1",
        refresh_token: "REFRESH-TOKEN-1",
        expires_in: "EXPIRES-IN-1"
      }
    })
    Client.add(client, function(err){
      assert.ifError(err)
      Client.getSize(function(err, count){
        assert.equal(count, 1)
        done()
      })
    })
  })

  it("[Client model]: Should be cleared after insert.", function (done) {
    Client.getSize(function (err, count) {
      assert.equal(count, 0)  //assert.equal(actual, expected)
      done()
    })
  })

  it("[Client model]: Reject insertion with not unique phone_id.", function (done) {
    let client = new Client({
      phone_id: "PHONE-ID-1",
      github_id: "GITHUB-ID-1",
      github: {
        access_token: "ACCESS-TOKEN-1",
        refresh_token: "REFRESH-TOKEN-1",
        expires_in: "EXPIRES-IN-1"
      }
    })
    let clientConflict = new Client({
      phone_id: "PHONE-ID-1", // <--same ID as client
      github_id: "GITHUB-ID-2",
      github: {
        access_token: "ACCESS-TOKEN-2",
        refresh_token: "REFRESH-TOKEN-2",
        expires_in: "EXPIRES-IN-2"
      }
    })
    Client.add(client, function(err){
      assert.ifError(err)
      Client.add(clientConflict, function(err2){
        expect(err2.phone_id).to.exists
        Client.getSize(function(err,count) {
          assert.equal(count, 1)  //assert.equal(actual, expected)
          done()
        })
      })
    })
  })

  it("[Client model]: Reject insertion with missing github_id", function (done) {
    let client = new Client({
      phone_id: "PHONE-ID-1",
      github_id: "",
      github: {
        access_token: "ACCESS-TOKEN-1",
        refresh_token: "REFRESH-TOKEN-1",
        expires_in: "EXPIRES-IN-1"
      }
    })
    Client.add(client, function(err){
      expect(err.phone_id).to.exists
        Client.getSize(function(err,count) {
          assert.equal(count, 0)  //assert.equal(actual, expected)
          done()
      })
    })
  })
/*
  it("[Client model]: TODO: getById", function (done) {
    assert.equal(1, 0)
    done()
  })

  it("[Client model]: TODO: getByPhoneId", function (done) {
    assert.equal(1, 0)
    done()
  })

  it("[Client model]: TODO: getByGithubId", function (done) {
    assert.equal(1, 0)
    done()
  })

  it("[Client model]: TODO: Update attributes", function (done) {
    assert.equal(1, 0)
    done()
  })

  it("[Client model]: TODO: RemoveById", function (done) {
    assert.equal(1, 0)
    done()
  })
*/
})

/*
it("getClientByPhoneId: populated", function (done) {
let client = new Client({ phone_id: 'PHONEIDSTRING', github_id: 'GITHUBIDSTRING'})
client.save()
Client.getClientByPhoneId(client.phone_id, function (err, userFromDB) {
//assert.equal(userFromDB.phone_id, "PHONEIDSTRING")
//assert.equal(userFromDB.github_id, "GITHUBIDSTRING")
done()
})
})
*/
