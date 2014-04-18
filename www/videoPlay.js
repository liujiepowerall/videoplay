var cordova = require('cordova'),
    exec = require('cordova/exec');

var VideoPlayer = function() {

};
VideoPlayer.prototype.play = function(url)
{
    exec(null, null, 'VideoPlayPlugin', 'playVideo', [url]);
};

var videoPlay = new VideoPlayer();

module.exports = videoPlay;