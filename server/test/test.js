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

  beforeEach("Clear clients and add two clients before each test", function (done) {
    // runs before each test in this block
    Client.remove({}, function(err, res){
      if(err != null){
        throw err
      }
      let client1 = new Client({
        phone_id: "PHONE-ID-1",
        github_id: "GITHUB-ID-1",
        github: {
          access_token: "ACCESS-TOKEN-1",
          refresh_token: "REFRESH-TOKEN-1",
          expires_in: "EXPIRES-IN-1"
        }
      })
      let client2 = new Client({
        phone_id: "PHONE-ID-2",
        github_id: "GITHUB-ID-2",
        github: {
          access_token: "ACCESS-TOKEN-2",
          refresh_token: "REFRESH-TOKEN-2",
          expires_in: "EXPIRES-IN-2"
        }
      })
      Client.insertMany([client1, client2]).then((docs) => {
        done()
      }).catch((err) => {
        throw err
      })
    })
  })

  afterEach(function() {
    // runs after each test in this block
  })


  //----------------------------------------------------------
  //          Actual tests begin here.
  //----------------------------------------------------------

  it("[Setup]: Clients should contain 2 documents.", function (done) {
    Client.getSize(function (err, count) {
      assert.equal(count, 2)  //assert.equal(actual, expected)
      done()
    })
  })

  it("[Client model]: Test add", function (done) {
    let client3 = new Client({
      phone_id: "PHONE-ID-3",
      github_id: "GITHUB-ID-3",
      github: {
        access_token: "ACCESS-TOKEN-3",
        refresh_token: "REFRESH-TOKEN-3",
        expires_in: "EXPIRES-IN-3"
      }
    })
    Client.add(client3, function(err){
      assert.ifError(err)
      Client.getSize(function(err, count){
        assert.equal(count, 3)
        done()
      })
    })
  })

  it("[Setup]: Should be cleared after insert.", function (done) {
    Client.getSize(function (err, count) {
      assert.equal(count, 2)
      done()
    })
  })

  it("[Client model]: Reject insertion with not unique phone_id.", function (done) {
    let clientConflict = new Client({
      phone_id: "PHONE-ID-1", // same as client1
      github_id: "GITHUB-ID-3",
      github: {
        access_token: "ACCESS-TOKEN-3",
        refresh_token: "REFRESH-TOKEN-3",
        expires_in: "EXPIRES-IN-3"
      }
    })
    Client.add(clientConflict, function(err){
      expect(err.phone_id).to.exists
      Client.getSize(function(err,count) {
        assert.equal(count, 2)
        done()
      })
    })
  })

  it("[Client model]: Reject insertion with missing github_id", function (done) {
    let clientMissing = new Client({
      phone_id: "PHONE-ID-3",
      github_id: "",
      github: {
        access_token: "ACCESS-TOKEN-3",
        refresh_token: "REFRESH-TOKEN-3",
        expires_in: "EXPIRES-IN-3"
      }
    })
    Client.add(clientMissing, function(err){
      expect(err.phone_id).to.exists
      Client.getSize(function(err,count) {
        assert.equal(count, 2)
        done()
      })
    })
  })

  it("[Client model]: Test getByPhoneId", function (done) {
    Client.getByPhoneId("PHONE-ID-2", function(err, clientFromDb){
      assert.equal(clientFromDb.phone_id, "PHONE-ID-2")
      assert.equal(clientFromDb.github_id, "GITHUB-ID-2")
      assert.equal(clientFromDb.github.access_token, "ACCESS-TOKEN-2")
      done()
    })
  })

  it("[Client model]: Test getById", function (done) {
    Client.getByPhoneId("PHONE-ID-1", function(err, clientFromDb){
      const id = clientFromDb.id
      Client.getById(id, function(err, userFromDB2){
        assert.equal(id, userFromDB2.id)
        assert.equal(userFromDB2.phone_id, "PHONE-ID-1")
        done()
      })
    })
  })

  it("[Client model]: Test RemoveById", function (done) {
    Client.getByPhoneId("PHONE-ID-2", function(err, clientFromDb){
      const id = clientFromDb.id
      Client.removeById(id, function(err,docs){
        assert.equal(docs.phone_id, "PHONE-ID-2")
        Client.getSize(function(err,count) {
          assert.equal(count, 1)
          Client.getByPhoneId("PHONE-ID-2", function(err, res){
            expect(res).to.be.null
            expect(err).to.be.null
            done()
          })
        })
      })
    })
  })

  it("[Client model]: Test getAll", function (done) {
    Client.getAll(function(err, clientsFromDb){
      assert.equal(clientsFromDb.length, 2)
      done()
    })
  })

  it("[Client model]: Test updateByGithubId", function (done) {
    let update = { "github.access_token": "ACCESS-TOKEN-UPDATED" }
    Client.updateByGithubId("GITHUB-ID-1", update, function(err, doc){
      expect(err).to.be.null;
      assert.equal(doc.phone_id, "PHONE-ID-1")
      assert.equal(doc.github_id, "GITHUB-ID-1")
      assert.equal(doc.github.access_token, "ACCESS-TOKEN-UPDATED")
      assert.equal(doc.github.refresh_token, "REFRESH-TOKEN-1")
      assert.equal(doc.github.expires_in, "EXPIRES-IN-1")
      Client.getSize(function(err,count) {
        assert.equal(count, 2)
        done()
      })
    })
  })

})
