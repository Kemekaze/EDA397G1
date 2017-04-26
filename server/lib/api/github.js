var request = require('request');

var Github  = module.exports = function(config) {
  var self = this;
  self.base_url = "https://api.github.com";
  self.token = new Buffer(config.username+":"+config.password).toString('base64');
  self.headers = {
    'User-Agent': 'request',
    "Accept": "application/vnd.github.inertia-preview+json",
    "Authorization": "Basic " + self.token
  };

};
var method = Github.prototype;

method.request = function(url,options,cb){
  request({
    url: this.base_url+url,
    headers: this.headers
  },function(error, response, body){
    cb(error,response, JSON.parse(body));
  });

}

method.info = function(cb){
  this.request('/user',{},cb);
}
method.repositories = function(cb){
  this.request('/user/repos',{},cb);
}
method.repository = function(owner_repo, cb){
  this.request('/repos/'+owner_repo,{},cb);
}
method.projects = function(owner_repo, cb){
  this.request('/repos/'+owner_repo+'/projects',{},cb);
}
method.project = function(project_id, cb){
  this.request('/projects/'+project_id,{},cb);
}
method.columns = function(project_id, cb){
  this.request('/projects/'+project_id+'/columns',{},cb);
}
method.cards = function(column_id, cb){
  this.request('/projects/columns/'+column_id+'/cards',{},cb);
}
method.issues = function(owner_repo, cb){
  this.request('/repos/'+owner_repo+'/issues',{},cb);
}
