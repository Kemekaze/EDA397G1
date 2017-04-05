module.exports = {
    key: require("fs").readFileSync('./config/ssl/key.pem'),
    cert: require("fs").readFileSync('./config/ssl/cert.pem'),
    passphrase: 'y41isuTGrFpoxFPX'
};
