const assert = require("assert")
const expect = require('chai').expect
const indexjs = require("../../lib")
const config = require("../../config/database")
const mongoose = require("mongoose")
const Client = mongoose.model('Client')
const Session = mongoose.model('Session')

describe('Session-model:', function() {

  // Runs before all tests in this block.
  before("Connect to database", function (done){
    mongoose.Promise = global.Promise
    mongoose.connect(config.database, function (error) {
      if (error){
        throw error // Handle failed connection
      }
      done()
    })
  })

  // Runs after all tests in this block.
  after(function(done) {
    mongoose.disconnect()
    done()
  })

  // runs before each test in this block
  beforeEach("Set up test data.", function (done) {
    // runs before each test in this block
    Session.remove({}, function(err, res){
      if(err != null){
        throw err
      }
      Client.remove({}, function(err, res){
        if(err != null){
          throw err
        }

        let client1 = new Client({
          //_id: 100,
          phone_id: "PHONE-ID-1",
          github_id: "GITHUB-ID-1",
          github: {}
        })
        let client2 = new Client({
          //_id: 200,
          phone_id: "PHONE-ID-2",
          github_id: "GITHUB-ID-2",
          github: {}
        })
        let client3 = new Client({
          //_id: 300,
          phone_id: "PHONE-ID-3",
          github_id: "GITHUB-ID-3",
          github: {}
        })
        Client.insertMany([client1, client2, client3]).then((docs) => {

        }).catch((err) => {
          throw err
        })

        let session1 = new Session({
          leader: "",
          clients: [],
          github: {
            repo_id: "githubrepoid1",
            project_id: "githubprojectid1",
            column_id: "githubcolumnid1",
            backlog_items:[]
          }
        })
        session1.save(err,function(){
          if(err != null){
            throw err
          }
          done()
        })
      })
    })
  })

  describe('[Setup check]', function() {

    it('Setup should contain 3 clients.', function(done) {
      Client.find().count(function(err, count){
        assert.equal(count, 3)
        done()
      })
    })

    it('Setup should contain 1 session.', function(done) {
      Session.find().count(function(err, count){
        assert.equal(count, 1)
        done()
      })
    })

  })

  describe.skip('#methodNameB()', function() {

    it('should return with value a', function() {

    })

    it('should return with value b', function() {

    })
  })

})
