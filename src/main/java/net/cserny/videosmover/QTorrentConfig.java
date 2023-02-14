package net.cserny.videosmover;

import org.eclipse.microprofile.config.inject.ConfigProperties;

import javax.enterprise.context.Dependent;

@Dependent
//@ConfigProperties(prefix = "qtorrent")
public class QTorrentConfig {

    private String torrentsUrl;

    public String getTorrentsUrl() {
        return torrentsUrl;
    }
}
