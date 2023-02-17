package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.Dependent;
import java.io.File;
import java.util.List;

@Dependent
public class QBitTorrentService implements TorrentService {

    private static final Logger LOGGER = Logger.getLogger(QBitTorrentService.class);

    @RestClient
    QBitTorrentV2ApiClient client;

    @Override
    public void delete(String sid, String hash, boolean deleteFiles) {
        client.delete(sid, hash, deleteFiles);
        LOGGER.info("Torrent deleted with hash " + hash);
    }

    @Override
    public List<TorrentFile> listFiles(String sid, String hash) {
        return client.files(sid, hash);
    }

    @Override
//    public void add(String sid, MultipartTorrent torrent) {
    public void add(String sid, File torrent) {
        client.add(sid, torrent);
    }
}
