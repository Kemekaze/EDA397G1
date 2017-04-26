const assert = require("assert")
const expect = require('chai').expect
const indexjs = require("../../lib")
const config = require("../../config/database")
const mongoose = require("mongoose")
const Client = mongoose.model('Client')

describe("Authenticate.github function:", function () {

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

  })

  afterEach(function() {
    // runs after each test in this block
  })


  describe('', function() {

    it('Setup should...', function(done) {
    })
  })


})
