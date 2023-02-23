package net.cserny.videosmover;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static net.cserny.videosmover.QBitTorrentV2ApiClient.SID_KEY;

@Dependent
public class QBitTorrentService implements TorrentService {

    private static final Logger LOGGER = Logger.getLogger(QBitTorrentService.class);

    @RestClient
    QBitTorrentV2ApiClient client;

    @Inject
    TorrentWebUIConfiguration configuration;

    @ConfigProperty(name = "video.mime.types")
    List<String> videoMimeTypes;

    @Override
    public String generateSid() {
        RestResponse<String> resp = client.login(configuration.username(), configuration.password());
        return resp.getCookies().get(SID_KEY).getValue();
    }

    @Override
    public void delete(String sid, String hash, boolean deleteFiles) {
        client.delete(sid, hash, deleteFiles);
        LOGGER.info("Torrent deleted with hash " + hash);
    }

    @Override
    public List<TorrentFile> listFiles(String sid, String hash) {
        List<TorrentFile> files = client.files(sid, hash);
        for (TorrentFile torrentFile : files) {
            Path torrentPath = Path.of(configuration.downloadRootPath(), torrentFile.getName());
            torrentFile.setMedia(isVideo(torrentPath));
        }
        return files;
    }

    private boolean isVideo(Path path) {
        String mimeType;
        try {
            mimeType = Files.probeContentType(path);
        } catch (IOException e) {
            return false;
        }

        for (String allowedType : videoMimeTypes) {
            if (allowedType.equals(mimeType)) {
                return true;
            }
        }

        return mimeType != null && mimeType.startsWith("video/");
    }
}
