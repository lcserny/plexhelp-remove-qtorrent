package net.cserny.videosmover;

import java.time.LocalDateTime;
import java.util.Objects;

public class TorrentFile {

    private String name;
    private long size;
    private LocalDateTime dateDownloaded;

    public TorrentFile() {
    }

    public TorrentFile(String name, long size, LocalDateTime dateDownloaded) {
        this.name = name;
        this.size = size;
        this.dateDownloaded = dateDownloaded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getDateDownloaded() {
        return dateDownloaded;
    }

    public void setDateDownloaded(LocalDateTime dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TorrentFile that = (TorrentFile) o;
        return size == that.size && Objects.equals(name, that.name) && Objects.equals(dateDownloaded, that.dateDownloaded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, dateDownloaded);
    }

    @Override
    public String toString() {
        return "TorrentFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", dateDownloaded=" + dateDownloaded +
                '}';
    }
}
