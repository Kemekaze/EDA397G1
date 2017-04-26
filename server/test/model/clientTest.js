const assert = require("assert")
const expect = require('chai').expect
const indexjs = require("../../lib")
const config = require("../../config/database")
const mongoose = require("mongoose")
const Client = mongoose.model('Client')

describe("Client-model:", function () {

  before("Connect to database", function (done){
    // runs before all tests in this block
    mongoose.Promise = global.Promise
    mongoose.connect(config.database, function (error) {
      if (error){
        throw error // Handle failed connection
      }else{
        //console.log('mongoose.connection.readyState:'+ mongoose.connection.readyState)
      }
      done()
    })
  })

  after(function(done) {
    // runs after all tests in this block
    mongoose.disconnect()
    done()
  })

  beforeEach("Set up test data", function (done) {
    // runs before each test in this block
    Client.remove({}, function(err, res){
      if(err != null){
        throw err
      }
      let client1 = new Client({
        phone_id: "PHONE-ID-1",
        github_id: "GITHUB-ID-1",
        github: {
          username: "USERNAME-1",
          password: "PASSWORD-1"
        }
      })
      let client2 = new Client({
        phone_id: "PHONE-ID-2",
        github_id: "GITHUB-ID-2",
        github: {
          username: "USERNAME-2",
          password: "PASSWORD-2"
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


  describe('[Setup check]', function() {
    it('Setup should contain 2 clients.', function(done) {
      Client.count({}, function(err, count){
        assert.equal(count, 2)
        done()
      })
    })
  })

  describe('#add()', function() {
    it('Should contain 3 clients after add', function(done) {
      let client3 = new Client({
        phone_id: "PHONE-ID-3",
        github_id: "GITHUB-ID-3",
        github: {
          username: "USERNAME-3",
          password: "PASSWORD-3"
        }
      })
      Client.add(client3, function(err){
        assert.ifError(err)
        Client.count({}, function(err, count){
          assert.equal(count, 3)
          done()
        })
      })
    })

    it("Should reject insertion with not unique phone_id.", function (done) {
      let clientConflict = new Client({
        phone_id: "PHONE-ID-1",
        github_id: "GITHUB-ID-3",
        github: {
          username: "USERNAME-3",
          password: "PASSWORD-3"
        }
      })
      Client.add(clientConflict, function(err){
        expect(err.phone_id).to.exists
        Client.count({}, function(err, count){
          assert.equal(count, 2)
          done()
        })
      })
    })

    it("Should reject insertion with missing github_id", function (done) {
      let clientMissing = new Client({
        phone_id: "PHONE-ID-3",
        github_id: "",
        github: {
          username: "USERNAME-3",
          password: "PASSWORD-3"
        }
      })
      Client.add(clientMissing, function(err){
        expect(err.phone_id).to.exists
        Client.count({}, function(err, count){
          assert.equal(count, 2)
          done()
        })
      })
    })
  })


  describe('#getByPhoneId()', function() {
    it("Should return the right client", function (done) {
      Client.getByPhoneId("PHONE-ID-2", function(err, clientFromDb){
        assert.equal(clientFromDb.phone_id, "PHONE-ID-2")
        assert.equal(clientFromDb.github_id, "GITHUB-ID-2")
        done()
      })
    })
  })

  describe('#removeById()', function() {
    it("Should remove the client.", function (done) {
      Client.getByPhoneId("PHONE-ID-2", function(err, clientFromDb){
        const id = clientFromDb.id
        Client.removeById(id, function(err,docs){
          assert.equal(docs.phone_id, "PHONE-ID-2")
          Client.count({}, function(err, count){
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
  })

  describe('#getAll()', function() {
    it("Should return all clients from the db", function (done) {
      Client.getAll(function(err, clientsFromDb){
        assert.equal(clientsFromDb.length, 2)
        done()
      })
    })
  })

  describe('#updateByGithubId()', function() {
    it("Client with github_id should have updated password.", function (done) {
      let update = { "github.password": "PASSWORD-UPDATED" }
      Client.updateByGithubId("GITHUB-ID-1", update, function(err, doc){
        expect(err).to.be.null;
        assert.equal(doc.phone_id, "PHONE-ID-1")
        assert.equal(doc.github_id, "GITHUB-ID-1")
        assert.equal(doc.github.username, "USERNAME-1")
        assert.equal(doc.github.password, "PASSWORD-UPDATED")
        Client.getSize(function(err,count) {
          assert.equal(count, 2)
          done()
        })
      })
    })
  })

})
