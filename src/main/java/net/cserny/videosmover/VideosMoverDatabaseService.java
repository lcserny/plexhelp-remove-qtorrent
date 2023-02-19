package net.cserny.videosmover;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

public interface VideosMoverDatabaseService {

    MongoCollection<Document> getDownloadCache();

    void updateDownloadsCache(List<TorrentFile> torrentFiles);
}
