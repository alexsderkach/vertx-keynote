var Router = require("vertx-web-js/router");
var StaticHandler = require("vertx-web-js/static_handler");
var SockJSHandler = require("vertx-web-js/sock_js_handler");

var staticHandler = StaticHandler.create();
var sockJSHandler = SockJSHandler.create(vertx);


var bridgeConfig = {
    "inboundPermitteds": [
        {
            "address" : "messages"
        },
        {
            "address" : "batch_requests"
        }
    ]
};

sockJSHandler.bridge(bridgeConfig);

var router = Router.router(vertx);
router.route("/ws/*").handler(sockJSHandler.handle);
router.route("/*").handler(staticHandler.handle);

vertx.createHttpServer()
    .requestHandler(router.accept)
    .listen(8080);
