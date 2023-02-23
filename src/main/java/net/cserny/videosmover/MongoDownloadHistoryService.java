package net.cserny.videosmover;

import com.mongodb.client.*;
import org.bson.Document;
import org.jboss.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

@Dependent
public class MongoDownloadHistoryService implements DownloadHistoryService {

    private static final Logger LOGGER = Logger.getLogger(MongoDownloadHistoryService.class);

    @Inject
    MongoConfiguration configuration;

    @Inject
    MongoClient client;

    public void updateDownloadsHistory(List<TorrentFile> torrentFiles) {
        MongoCollection<Document> collection = getDownloadHistory();

        String dateDownloaded = ZonedDateTime.now().format(RFC_1123_DATE_TIME);

        List<Document> docs = torrentFiles.stream()
                .filter(TorrentFile::isMedia)
                .map(torrentFile -> new Document()
                    .append("file_name", torrentFile.getName())
                    .append("file_size", torrentFile.getSize())
                    .append("date_downloaded", dateDownloaded))
                .collect(Collectors.toList());

        collection.insertMany(docs);

        LOGGER.info("Cache updated for " + collection + " collection");
    }

    public MongoCollection<Document> getDownloadHistory() {
        MongoDatabase database = client.getDatabase(configuration.db());
        createCollection(database, configuration.collection());
        return database.getCollection(configuration.collection());
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
            LOGGER.info("Mongo collection " + collection + " doesn't exist, creating...");
            database.createCollection(collection);
        }
    }
}
