package net.cserny.videosmover;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "torrent.webui")
public class TorrentWebUIConfiguration {

    private String baseUrl;
    private String username;
    private String password;
    private String downloadRootPath;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDownloadRootPath() {
        return downloadRootPath;
    }

    public void setDownloadRootPath(String downloadRootPath) {
        this.downloadRootPath = downloadRootPath;
    }
}
