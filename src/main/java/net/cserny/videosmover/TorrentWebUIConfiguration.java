package net.cserny.videosmover;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "torrent.webui")
public interface TorrentWebUIConfiguration {

    String username();

    String password();

    @WithName("download.root.path")
    String downloadRootPath();
}
