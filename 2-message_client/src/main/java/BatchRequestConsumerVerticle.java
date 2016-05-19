import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.redis.RedisClient;

public class BatchRequestConsumerVerticle extends AbstractVerticle {

  private RedisClient redisClient;

  @Override
  public void start() throws Exception {

    redisClient = RedisClient.create(vertx);

    vertx.eventBus().<JsonObject>consumer("batch_requests")
        .handler(this::handleRequest);
  }

  private void handleRequest(Message<JsonObject> message) {
    Long from = message.body().getLong("from");

    redisClient.llenObservable("messages")
        .flatMap(len -> redisClient.lrangeObservable("messages", from, len))
        .subscribe(
            message::reply,
            Throwable::printStackTrace
        );
  }
}
