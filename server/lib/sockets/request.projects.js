//request.projects.js
var response = require('../responses');

module.exports = function (socket,data,callback){
  //mock data in correct form
  callback(response.OK(
    {
    	"repos": [{
    		"id": "REPOID1",
    		"name": "Reponame1",
    		"full_name": "dummyname/Reponame1",
    		"private": true,
    		"projects": [{
    			"id": "PROJECTID1A",
    			"name": "ProjectAInRepo1"
    		}, {
    			"id": "PROJECTID1B",
    			"name": "ProjectBInRepo1"
    		}, {
    			"id": "PROJECTID1C",
    			"name": "ProjectCInRepo1"
    		}]
    	}, {
    		"id": "REPOID2",
    		"name": "Reponame2",
    		"full_name": "otherdummyname/Reponame2",
    		"private": false,
    		"projects": [{
    			"id": "PROJECTID2A",
    			"name": "ProjectAInRepo2"
    		}, {
    			"id": "PROJECTID2B",
    			"name": "ProjectBInRepo2"
    		}]
    	}]
    }
  ))

};
