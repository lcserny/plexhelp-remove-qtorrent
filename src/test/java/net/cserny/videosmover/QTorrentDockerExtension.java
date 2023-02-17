package net.cserny.videosmover;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;

public class QTorrentDockerExtension implements BeforeAllCallback, AfterAllCallback {

    static int QTORRENT_PORT = 7099;

    private GenericContainer<?> qbittorrentContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        qbittorrentContainer = new GenericContainer<>("linuxserver/qbittorrent:4.5.0")
                .withExposedPorts(QTORRENT_PORT)
                .withEnv("WEBUI_PORT", String.valueOf(QTORRENT_PORT))
                .waitingFor(Wait.forHttp("/").forPort(QTORRENT_PORT));
        qbittorrentContainer.setPortBindings(List.of(QTORRENT_PORT + ":" + QTORRENT_PORT));
        qbittorrentContainer.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        qbittorrentContainer.stop();
    }
}
