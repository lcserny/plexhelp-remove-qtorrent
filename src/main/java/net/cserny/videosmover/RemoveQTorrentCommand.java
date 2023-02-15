package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import javax.inject.Inject;

@CommandLine.Command(name = "remove-qtorrent", mixinStandardHelpOptions = true)
public class RemoveQTorrentCommand implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RemoveQTorrentCommand.class);

    @RestClient
    QTorrentV2ApiClient qTorrentV2TorrentsClient;

    @Inject
    MongoService mongoService;

    @CommandLine.Option(names = {"-h", "-hash"}, required = true, description = "QBittorrent hash of the torrent to remove")
    private String hash;

    @Override
    public void run() {
        // use $PWD/config/application.properties for prod overrides

        LOGGER.info("Hash passed: " + hash);

        mongoService.updateCache(new TorrentFile());
//        qTorrentV2TorrentsClient.delete("sdfsdfs", false);
    }
}
