package net.cserny.videosmover;

import com.mongodb.client.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import java.time.Instant;

@Service
@Slf4j
public class MongoDownloadHistoryService implements DownloadHistoryService {

    @Value("${mongo.collection}")
    String mongoCollection;

    @Autowired
    MongoTemplate mongoTemplate;

    public void updateDownloadsHistory(List<TorrentFile> torrentFiles) {
        MongoCollection<Document> collection = getDownloadHistory();

        List<Document> docs = torrentFiles.stream()
                .filter(TorrentFile::isMedia)
                .map(torrentFile -> new Document()
                    .append("file_name", torrentFile.getName())
                    .append("file_size", torrentFile.getSize())
                    .append("date_downloaded", Instant.now()))
                .collect(Collectors.toList());

        if (!docs.isEmpty()) {
            collection.insertMany(docs);
            log.info("Cache updated for " + collection + " collection");
        } else {
            log.info("No media files found to insert in cache");
        }
    }

    public MongoCollection<Document> getDownloadHistory() {
        MongoDatabase database = mongoTemplate.getMongoDatabaseFactory().getMongoDatabase();
        createCollection(database, mongoCollection);
        return database.getCollection(mongoCollection);
    }

    private void createCollection(MongoDatabase database, String collection) {
        boolean collectionExists;
        try (MongoCursor<String> collections = database.listCollectionNames().iterator()) {
            collectionExists = false;
            while (collections.hasNext()) {
                if (collections.next().equals(collection)) {
                    collectionExists = true;
                    break;
                }
            }
        }

        if (!collectionExists) {
            log.info("Mongo collection " + collection + " doesn't exist, creating...");
            database.createCollection(collection);
        }
    }
}
