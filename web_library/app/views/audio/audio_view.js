define(["require", "exports"], function (require, exports) {
    "use strict";
    var AudioView = (function () {
        function AudioView(audioMechanism, container, distPath) {
            this.audioMechanism = audioMechanism;
            this.container = container;
            this.distPath = distPath;
            this.initElements();
            this.initEvents();
        }
        AudioView.prototype.initElements = function () {
            this.recordButton = this.container.find('a.record');
            this.stopButton = this.container.find('a.stop');
            this.deleteButton = this.container.find('a.delete');
            this.replayButton = this.container.find('a.replay');
            this.audioElement = this.container.find('audio.audio');
        };
        AudioView.prototype.initEvents = function () {
            var myThis = this;
            this.recordButton.on('click', function () {
                Fr.voice.record(false, function () {
                    myThis.recordButton.addClass("disabled");
                    var activeSrc = myThis.recordButton.find('img').data('active-src');
                    myThis.recordButton.find('img').attr('src', activeSrc);
                    myThis.container.find(".one").removeClass("disabled");
                });
            });
            this.stopButton.on('click', function () {
                Fr.voice.pause();
                Fr.voice.export(function (url) {
                    myThis.audioElement.attr("src", url);
                }, "URL");
            });
            this.deleteButton.on('click', function () {
                myThis.audioElement.attr("src", null);
                myThis.restore();
            });
            this.replayButton.on('click', function () {
                Fr.voice.export(function (url) {
                    myThis.audioElement.attr("src", url);
                    myThis.audioElement[0].play();
                }, "URL");
            });
        };
        AudioView.prototype.restore = function () {
            this.recordButton.removeClass("disabled");
            this.container.find('.one').addClass("disabled");
            Fr.voice.stop();
        };
        AudioView.prototype.getBlob = function (callback) {
            Fr.voice.export(function (blob) {
                callback(blob);
            }, "blob");
        };
        return AudioView;
    }());
    exports.AudioView = AudioView;
});
//# sourceMappingURL=audio_view.js.map