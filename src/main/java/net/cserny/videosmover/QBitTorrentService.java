package net.cserny.videosmover;

import lombok.extern.slf4j.Slf4j;
import net.cserny.videosmover.TorrentFile.TorrentFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class QBitTorrentService implements TorrentService {

    @Autowired
    QBitTorrentV2ApiClient client;

    @Autowired
    TorrentWebUIConfiguration configuration;

    @Autowired
    VideoConfiguration videoConfiguration;

    @Override
    public String generateSid() {
        ResponseEntity<String> response = client.login(configuration.getUsername(), configuration.getPassword());

        String cookies = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        String sid = cookies.substring(4, cookies.indexOf(";"));
        log.info("SID generated: " + sid);

        return sid;
    }

    @Override
    public void delete(String sid, String hash, boolean deleteFiles) {
        client.delete(sid, hash, deleteFiles);
        log.info("Torrent deleted with hash " + hash);
    }

    @Override
    public List<TorrentFile> listFiles(String sid, String hash) {
        ResponseEntity<TorrentFiles> response = client.files(sid, hash);
        TorrentFiles files = response.getBody();
        for (TorrentFile torrentFile : files) {
            Path torrentPath = Path.of(configuration.getDownloadRootPath(), torrentFile.getName());
            torrentFile.setMedia(isVideo(torrentPath));
        }
        log.info("Files found: " + files);
        return files;
    }

    private boolean isVideo(Path path) {
        String mimeType;
        try {
            mimeType = Files.probeContentType(path);
        } catch (IOException e) {
            return false;
        }

        for (String allowedType : videoConfiguration.getMimeTypes()) {
            if (allowedType.equals(mimeType)) {
                return true;
            }
        }

        return mimeType != null && mimeType.startsWith("video/");
    }
}
