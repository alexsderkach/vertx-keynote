import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    DeploymentOptions options = new DeploymentOptions();
    options.setInstances(2);
    options.setWorker(true);
    vertx.deployVerticle(Verticle.class.getName(), options);
  }
}
