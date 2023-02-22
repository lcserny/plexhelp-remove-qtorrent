package net.cserny.videosmover;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Map;

// Please note all test classes will share this same mongo instance
public class MongoTestSetup implements QuarkusTestResourceLifecycleManager {

    public MongoDBContainer container;

    @Override
    public Map<String, String> start() {
        container = new MongoDBContainer("mongo:5.0");
        container.start();
        return Map.of("quarkus.mongodb.connection-string", container.getConnectionString());
    }

    @Override
    public void stop() {
        container.stop();
    }
}
