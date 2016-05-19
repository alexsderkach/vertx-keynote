var socketUrl = 'http://' + window.location.host + '/ws';
var eventBus = new vertx.EventBus(socketUrl);
var timer;
var messageCount = 0;

eventBus.onopen = function () {
    showMessage('Opened connection to ' + socketUrl);

    timer = setInterval(function () {
        eventBus.send("batch_requests", {'from': messageCount}, function (batch) {
            if (batch.length > 0) {
                showMessage("Received batch");
                for (var i in batch) {
                    showMessage(batch[i]);
                }
                messageCount += batch.length;
            }
        });
    }, 1000);
};

eventBus.onclose = function () {
    showMessage('Closed connection to ' + socketUrl);
    clearInterval(timer);
};

function sendMessage(e) {
    e.preventDefault();

    var username = document.getElementById("username").value;
    var message = document.getElementById("message").value;

    eventBus.send("messages", {'username': username, 'message': message});
}

function showMessage(message) {
    var element = document.createElement("pre");
    element.innerHTML = message;
    document.body.appendChild(element);
}