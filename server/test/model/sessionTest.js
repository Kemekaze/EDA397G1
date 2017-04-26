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

        let client1Leader = new Client({
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
        Client.insertMany([client1Leader, client2, client3]).then((docs) => {
          let session1 = new Session({
            leader: client1Leader,
            clients: [client2._id],
            github: {
              repo_id: "githubrepoid1",
              project_id: "githubprojectid1",
              column_id: "githubcolumnid1",
              backlog_items:[]
            }
          })
          session1.save(function(err, doc){
            expect(err).to.be.null
            expect(doc).to.exist;
            done()
          })
        }).catch((err) => {
          throw err
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

    it('Session should contain client1 as leader.', function(done) {
      Client.getByPhoneId("PHONE-ID-1", function(err, leaderClient){
        Session.findOne({leader: leaderClient._id})
        .populate('leader')
        .exec(function (err, session) {
          expect(err).to.not.exist
          assert.equal(session.leader.github_id, "GITHUB-ID-1")
          done()
        })
      })
    })
  })

  describe('#createSession()', function() {

    it("Should work with client object", function(done) {
      let leader = new Client({
        phone_id: "PHONE-ID-4",
        github_id: "GITHUB-ID-4",
        github: {}
      })

      Session.createSession(leader, "repoid", "projectid", "columnid", function(err){
        Session.find().count(function(err, count){
          expect(err).to.not.exist
          assert.equal(count, 2)
          done();
        })
      })
    })

    it('Should work with client id.', function(done) {

      let client = new Client({
        phone_id: "PHONE-ID-4",
        github_id: "GITHUB-ID-4"
      })
      Client.add(client, function(err){

        Client.findOne({phone_id: "PHONE-ID-4"}, function(err, doc){
          Session.createSession(doc._id, "repoid2", "projectid2", "columnid2", function(err){
            expect(err).to.not.exist
            Session.find().count(function(err, count){
              assert.equal(count, 2)
              done();
            })
          })
        })
      })
    })

    it('Should fail with invalid leader object.', function(done) {
      Session.createSession("someinvalidobject", "repoid2", "projectid2", "columnid2", function(err){
        expect(err).to.exist
        Session.find().count(function(err, count){
          assert.equal(count, 1)
          done();
        })
      })
    })

  })
/*
  describe.skip('#addClient()', function(done) {
    it('create with object id', function() {
      done
    })
  })
*/
})
