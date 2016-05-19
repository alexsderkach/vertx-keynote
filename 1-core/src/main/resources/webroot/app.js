var socketUrl = 'ws://' + window.location.host + '/';
var webSocket = new WebSocket(socketUrl);
var timer;

webSocket.onopen = function () {
    showMessage('Opened connection to ' + socketUrl);

    timer = setInterval(function () {
        webSocket.send("some data");
    }, 1000);
};

webSocket.onmessage = function (e) {
    showMessage(e.data);
};

webSocket.onclose = function () {
    showMessage('Closed connection to ' + socketUrl);
    clearInterval(timer);
};

function showMessage(message) {
    var element = document.createElement("pre");
    element.innerHTML = message;
    document.body.appendChild(element);
}