package net.cserny.videosmover;

import java.time.LocalDateTime;

public class DownloadedTorrent {

    private String name;
    private LocalDateTime dateDownloaded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateDownloaded() {
        return dateDownloaded;
    }

    public void setDateDownloaded(LocalDateTime dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }
}
