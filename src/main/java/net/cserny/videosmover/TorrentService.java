package net.cserny.videosmover;

import java.util.List;

public interface TorrentService {

    String generateSid();

    void delete(String sid, String hash, boolean deleteFiles);

    List<TorrentFile> listFiles(String sid, String hash);
}
