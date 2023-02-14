package net.cserny.videosmover;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@CommandLine.Command(name = "remove-qtorrent", mixinStandardHelpOptions = true)
public class RemoveQTorrentCommand implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RemoveQTorrentCommand.class);

    private final QTorrentConfig qTorrentConfig;
    private final MongoConfig mongoConfig;

    @Inject
    public RemoveQTorrentCommand(QTorrentConfig qTorrentConfig, MongoConfig mongoConfig) {
        this.qTorrentConfig = qTorrentConfig;
        this.mongoConfig = mongoConfig;
    }

    @CommandLine.Option(names = {"-h", "-hash"}, description = "QBittorrent hash of the torrent to remove")
    private String hash;

    @Override
    public void run() {
        // TODO: inject config properties class, cause it doesn't work with @Dependent

        // use $PWD/config/application.properties for prod overrides
        LOGGER.info("Hash passed: " + hash);
        LOGGER.info("Mongo db = " + mongoConfig.getDb());
    }
}
