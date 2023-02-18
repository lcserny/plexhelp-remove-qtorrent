package net.cserny.videosmover;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@Testcontainers
public class MongoServiceTest {

    @RegisterExtension
    static final MongoDockerExtension deploy = new MongoDockerExtension();

    @Inject
    MongoService mongoService;

    @Inject
    MongoClient mongoClient;

    @Inject
    MongoConfiguration configuration;

    // FIXME: auth failed, maybe set docker mongo user and pass when creating through env vars?
    @Test
    @DisplayName("Check that service can add to download cache")
    public void serviceCanUpdateCache() {
        String name = "name1";
        int size = 1;
        List<TorrentFile> torrentFiles = List.of(new TorrentFile(name, size));

        mongoService.updateCache(torrentFiles);

        MongoCollection<Document> collection = mongoClient.getDatabase(configuration.db())
                .getCollection(configuration.collection());

        assertThat("collection is empty", collection.countDocuments() == 1L);

        Document docToFind = new Document(Map.of("file_name", name, "file_size", size));

        FindIterable<Document> documents = collection.find(docToFind);
        for (Document document : documents) {
            assertThat("document has wrong name", document.get("file_name").equals(name));
            assertThat("document has wrong size", document.get("file_size").equals(size));
            assertThat("document has no date", document.get("date_downloaded") != null);
        }
    }
}
