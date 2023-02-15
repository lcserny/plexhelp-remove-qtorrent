package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import javax.inject.Inject;

@CommandLine.Command(name = "remove-qtorrent", mixinStandardHelpOptions = true)
public class RemoveQTorrentCommand implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RemoveQTorrentCommand.class);

    @RestClient
    QTorrentV2TorrentsClient qTorrentV2TorrentsClient;

    @Inject
    MongoConfiguration mongoConfiguration;

    @CommandLine.Option(names = {"-h", "-hash"}, description = "QBittorrent hash of the torrent to remove")
    private String hash;

    @Override
    public void run() {
        // use $PWD/config/application.properties for prod overrides

        LOGGER.info("Hash passed: " + hash);
        LOGGER.info("Mongo db = " + mongoConfiguration.db());

        qTorrentV2TorrentsClient.delete("sdfsdfs", false);
    }
}
