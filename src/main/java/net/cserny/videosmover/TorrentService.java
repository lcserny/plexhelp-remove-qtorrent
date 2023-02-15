package net.cserny.videosmover;

import java.util.List;

public interface TorrentService {

    void delete(String hash, boolean deleteFiles);

    List<TorrentFile> listFiles(String hash);
}
