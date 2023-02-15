package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class QBitTorrentService implements TorrentService {

    private static final Logger LOGGER = Logger.getLogger(QBitTorrentService.class);

    @RestClient
    QBitTorrentV2ApiClient client;

    @Override
    public void delete(String hash, boolean deleteFiles) {
        client.delete(hash, deleteFiles);
        LOGGER.info("Torrent deleted with hash " + hash);
    }

    @Override
    public List<TorrentFile> listFiles(String hash) {
        return client.files(hash);
    }
}
