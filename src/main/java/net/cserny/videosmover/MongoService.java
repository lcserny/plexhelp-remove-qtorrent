package net.cserny.videosmover;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MongoService {

    @Inject
    MongoConfiguration configuration;

    @Inject
    MongoClient client;

    public void updateCache(TorrentFile torrent) {
        Document document = new Document()
                .append("name", torrent.getName())
                .append("dateDownloaded", torrent.getDateDownloaded());

        getDownloadCacheCollection().insertOne(document);
    }

    private MongoCollection<Document> getDownloadCacheCollection() {
        return client.getDatabase(configuration.db()).getCollection(configuration.collection());
    }
}
