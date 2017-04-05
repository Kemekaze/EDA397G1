var assert = require("assert");
var indexjs = require("../index.js");


describe("MongoDB", function () {
  const mongoose = require("mongoose");
  before("Connect to database", function () {
  });
});










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
