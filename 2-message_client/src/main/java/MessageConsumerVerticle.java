import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.redis.RedisClient;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class MessageConsumerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    RedisClient redisClient = RedisClient.create(vertx);

    EventBus eventBus = vertx.eventBus();

    ConnectableObservable<JsonObject> messages = eventBus.<JsonObject>consumer("messages")
        .bodyStream()
        .toObservable()
        .publish();

    Observable<Long> idStream = messages
        .flatMap(message -> redisClient.incrObservable("id_generator"));

    Observable<JsonObject> timedMessages = messages
        .map(message -> message.put("time", LocalDateTime.now().toString()));

    Observable.zip(idStream, timedMessages, (id, message) -> message.put("id", id))
        .buffer(2, TimeUnit.SECONDS)
        .filter(list -> !list.isEmpty())
        .map(list -> list.stream().map(JsonObject::toString).collect(toList()))
        .flatMap(batch -> redisClient.rpushManyObservable("messages", batch))
        .subscribe(
            count -> System.out.println("Total messages count: " + count),
            Throwable::printStackTrace
        );

    messages.connect();
  }
}
