var exports = module.exports = {};
var response = exports.response = function (status, payload, errors) {
  if(typeof status != "number" && isNaN(status)) throw ('Status is not an Integer');
  if(!((!!payload) && (payload.constructor === Object)) && !Array.isArray(payload)) throw ('Payload is not an Object');
  if(!Array.isArray(errors)) throw new Error('Errors is not an Array');
  return {
    status: status,
    data:   payload,
    errors: errors
  };
};
var response_with_error = function (status, payload, error, message, errors) {
  if(typeof payload === 'string'){
    if(typeof message !== undefined){
      errors = message;
    }
    message = payload;
    payload = {};
  }
  var err= [{
    error: error,
    message: message
  }];
  if (errors !== undefined){
    if(!Array.isArray(errors)) throw new Error('Errors is not an Array');
    err = err.concat(errors)
  }
  return response(status,payload,err);
};

exports.OK = function (payload){
  return response(200,payload,[]);
}
exports.BAD_REQUEST = function (payload,message,errors){
  return response_with_error(400,payload,'Bad Request',message,errors);
}
exports.UNAUTHORIZED = function (payload,message,errors){
  return response_with_error(401,payload,'Unauthorized',message,errors);
}
exports.FORBIDDEN = function (payload,message,errors){
  return response_with_error(403,payload,'Forbidden',message,errors);
}
exports.NOT_FOUND = function (payload,message,errors){
  return response_with_error(404,payload,'Not Found',message,errors);
}
exports.SERVER_ERROR = function (payload,message,errors){
  return response_with_error(500,payload,'Server Error',message,errors);
}
exports.NOT_IMPLEMENTED = function (payload,message,errors){
  return response_with_error(501,payload,'Not Implemented',message,errors);
}
exports.SERVICE_UNAVAILABLE = function (payload,message,errors){
  return response_with_error(504,payload,'Service Unavailable',message,errors);
}
