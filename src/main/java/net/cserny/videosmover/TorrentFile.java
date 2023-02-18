package net.cserny.videosmover;

import java.util.Objects;

public class TorrentFile {

    private String name;
    private long size;

    public TorrentFile() {
    }

    public TorrentFile(String name, long size) {
        this.name = name;
        this.size = size;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TorrentFile that = (TorrentFile) o;
        return size == that.size && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }

    @Override
    public String toString() {
        return "TorrentFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
