package net.cserny.videosmover;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "torrent.webui")
public interface TorrentWebUIConfiguration {

    String username();

    String password();
}
