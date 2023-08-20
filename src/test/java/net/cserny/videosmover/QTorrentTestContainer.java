package net.cserny.videosmover;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;

public class QTorrentTestContainer extends GenericContainer {

    public static int QTORRENT_PORT = 7099;
    private static QTorrentTestContainer container;

    public QTorrentTestContainer() {
        super("linuxserver/qbittorrent:4.5.2");
        setExposedPorts(List.of(QTORRENT_PORT));
        setEnv(List.of("WEBUI_PORT=" + QTORRENT_PORT));
        setPortBindings(List.of(QTORRENT_PORT + ":" + QTORRENT_PORT));
        waitingFor(Wait.forHttp("/").forPort(QTORRENT_PORT));
    }

    public static QTorrentTestContainer getInstance() {
        if (container == null) {
            container = new QTorrentTestContainer();
        }
        return container;
    }
}
