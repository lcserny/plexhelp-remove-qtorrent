package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

import static net.cserny.videosmover.QBitTorrentV2ApiClient.SID_KEY;

@Dependent
public class QBitTorrentService implements TorrentService {

    private static final Logger LOGGER = Logger.getLogger(QBitTorrentService.class);

    @RestClient
    QBitTorrentV2ApiClient client;

    @Inject
    TorrentWebUIConfiguration configuration;

    @Override
    public String generateSid() {
        Response resp = client.login(configuration.username(), configuration.password());
        return resp.getCookies().get(SID_KEY).getValue();
    }

    @Override
    public void delete(String sid, String hash, boolean deleteFiles) {
        client.delete(sid, hash, deleteFiles);
        LOGGER.info("Torrent deleted with hash " + hash);
    }

    @Override
    public List<TorrentFile> listFiles(String sid, String hash) {
        return client.files(sid, hash);
    }
}
