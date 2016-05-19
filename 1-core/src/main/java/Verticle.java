import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

import static java.lang.Thread.currentThread;

public class Verticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    Handler<RoutingContext> staticHandler = StaticHandler.create();

    Router router = Router.router(vertx);
    router.route("/*").handler(staticHandler);

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .websocketHandler(this::handleWebSocket)
        .listen(8080);
  }

  private void handleWebSocket(ServerWebSocket serverWebSocket) {
    serverWebSocket.writeFinalTextFrame("Handling connection in thread: " + currentThread().getName());

    serverWebSocket.handler(buffer -> {
      serverWebSocket.writeFinalTextFrame("Handling connection in thread: " + currentThread().getName());
    });
  }
}
