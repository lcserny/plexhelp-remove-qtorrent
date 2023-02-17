package net.cserny.videosmover;

import picocli.CommandLine;

import javax.inject.Inject;
import java.util.List;

@CommandLine.Command(name = "remove-qtorrent", mixinStandardHelpOptions = true)
public class RemoveQTorrentCommand implements Runnable {

    @Inject
    TorrentService torrentService;

    @Inject
    CacheUpdater cacheUpdater;

    @CommandLine.Option(names = {"-h", "-hash"}, required = true, description = "QBittorrent hash of the torrent to remove")
    private String hash;

    @Override
    public void run() {
        // relies on unauthenticated operations in QTorrent WebUI, no SID passed
        List<TorrentFile> files = torrentService.listFiles(null, hash);
        cacheUpdater.updateCache(files);
        torrentService.delete(null, hash, false);
    }
}
