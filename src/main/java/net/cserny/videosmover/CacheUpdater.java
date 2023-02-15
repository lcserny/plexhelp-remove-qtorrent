package net.cserny.videosmover;

import java.util.List;

public interface CacheUpdater {

    void updateCache(List<TorrentFile> torrentFiles);
}
