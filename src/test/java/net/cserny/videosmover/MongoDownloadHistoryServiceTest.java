package net.cserny.videosmover;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
public class MongoDownloadHistoryServiceTest {

    @Container
    public static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:5.0");

    @DynamicPropertySource
    public static void qTorrentProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoContainer.getConnectionString());
    }

    @Autowired
    MongoDownloadHistoryService service;

    @Test
    @DisplayName("Check that service can add to download cache")
    public void serviceCanUpdateCache() {
        String name = "name1";
        long size = 1L;
        List<TorrentFile> torrentFiles = List.of(new TorrentFile(name, size, true));

        service.updateDownloadsHistory(torrentFiles);

        MongoCollection<Document> collection = service.getDownloadHistory();

        assertThat("collection is empty", collection.countDocuments() == 1L);

        Document docToFind = new Document(Map.of("file_name", name, "file_size", size));

        MongoCursor<Document> documentsCursor = collection.find(docToFind).iterator();

        try (documentsCursor) {
            assertThat("found documents count doesn't match", documentsCursor.available() == 1);

            while (documentsCursor.hasNext()) {
                Document document = documentsCursor.next();
                assertThat("document has wrong name", document.get("file_name").equals(name));
                assertThat("document has wrong size", document.get("file_size").equals(size));
                assertThat("document has no date", document.get("date_downloaded") != null);
            }
        }
    }
}
