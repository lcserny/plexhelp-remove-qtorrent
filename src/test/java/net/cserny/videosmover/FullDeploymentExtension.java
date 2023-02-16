package net.cserny.videosmover;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;

public class FullDeploymentExtension implements BeforeAllCallback, AfterAllCallback {

    static int QTORRENT_PORT = 7099;
    static int MONGO_PORT = 37017;

    private Network network;
    private GenericContainer<?> qbittorrentContainer;
    private GenericContainer<?> mongoDbContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        network = Network.newNetwork();

        qbittorrentContainer = new GenericContainer<>("linuxserver/qbittorrent:4.5.0")
                .withExposedPorts(QTORRENT_PORT)
                .withEnv("WEBUI_PORT", String.valueOf(QTORRENT_PORT))
                .withNetwork(network)
                .waitingFor(Wait.forHttp("/").forPort(QTORRENT_PORT));
        qbittorrentContainer.setPortBindings(List.of(QTORRENT_PORT + ":" + QTORRENT_PORT));
        qbittorrentContainer.start();

        // TODO: mongo healthcheck doesnt work
        mongoDbContainer = new GenericContainer<>("mongo:5.0")
                .withExposedPorts(MONGO_PORT)
                .withNetwork(network)
                .waitingFor(Wait.forHttp("/test").forPort(MONGO_PORT));
        mongoDbContainer.setPortBindings(List.of(MONGO_PORT + ":27017"));
        mongoDbContainer.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        qbittorrentContainer.stop();
        mongoDbContainer.stop();
    }
}
