package net.cserny.videosmover;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;

public class MongoDockerExtension implements BeforeAllCallback, AfterAllCallback {

    static int MONGO_PORT = 37017;

    private GenericContainer<?> mongoDbContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        mongoDbContainer = new GenericContainer<>("mongo:5.0")
                .withExposedPorts(MONGO_PORT)
                .waitingFor(Wait.forLogMessage(".*MongoDB starting.*", 1));
        mongoDbContainer.setPortBindings(List.of(MONGO_PORT + ":27017"));
        mongoDbContainer.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        mongoDbContainer.stop();
    }
}
