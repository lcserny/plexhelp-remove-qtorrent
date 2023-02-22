package net.cserny.videosmover;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

public interface DownloadHistoryService {

    MongoCollection<Document> getDownloadHistory();

    void updateDownloadsHistory(List<TorrentFile> torrentFiles);
}
