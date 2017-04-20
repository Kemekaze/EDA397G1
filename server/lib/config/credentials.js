module.exports = {
    key: require("fs").readFileSync(serverRoot+'/config/ssl/key.pem'),
    cert: require("fs").readFileSync(serverRoot+'/config/ssl/cert.pem'),
    passphrase: 'y41isuTGrFpoxFPX'
};
