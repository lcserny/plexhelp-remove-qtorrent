package net.cserny.videosmover;

import java.io.File;
import java.util.List;

public interface TorrentService {

    void delete(String sid, String hash, boolean deleteFiles);

    List<TorrentFile> listFiles(String sid, String hash);

//    void add(String sid, MultipartTorrent torrent);
    void add(String sid, File torrent);
}
