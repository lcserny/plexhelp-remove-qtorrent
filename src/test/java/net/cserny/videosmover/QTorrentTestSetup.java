package net.cserny.videosmover;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;
import java.util.Map;

public class QTorrentTestSetup implements QuarkusTestResourceLifecycleManager {

    static int QTORRENT_PORT = 7099;

    private GenericContainer<?> qbittorrentContainer;

    @Override
    public Map<String, String> start() {
        qbittorrentContainer = new GenericContainer<>("linuxserver/qbittorrent:4.5.0")
                .withExposedPorts(QTORRENT_PORT)
                .withEnv("WEBUI_PORT", String.valueOf(QTORRENT_PORT))
                .waitingFor(Wait.forHttp("/").forPort(QTORRENT_PORT));
        qbittorrentContainer.setPortBindings(List.of(QTORRENT_PORT + ":" + QTORRENT_PORT));
        qbittorrentContainer.start();
        return Map.of("quarkus.rest-client.\"net.cserny.videosmover.QBitTorrentV2ApiClient\".url", "http://localhost:" + QTORRENT_PORT);
    }

    @Override
    public void stop() {
        qbittorrentContainer.stop();
    }
}
