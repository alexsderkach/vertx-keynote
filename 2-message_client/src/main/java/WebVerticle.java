import io.vertx.core.Handler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.StaticHandler;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;

public class WebVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    Handler<RoutingContext> staticHandler = StaticHandler.create();

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

    BridgeOptions bridgeOptions = new BridgeOptions()
        .addInboundPermitted(new PermittedOptions().setAddress("messages"))
        .addInboundPermitted(new PermittedOptions().setAddress("batch_requests"));

    sockJSHandler.bridge(bridgeOptions);

    Router router = Router.router(vertx);
    router.route("/ws/*").handler(sockJSHandler);
    router.route("/*").handler(staticHandler);

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
  }
}
