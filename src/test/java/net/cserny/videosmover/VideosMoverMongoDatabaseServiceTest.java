package net.cserny.videosmover;

import com.mongodb.client.FindIterable;
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
public class VideosMoverMongoDatabaseServiceTest {

    @RegisterExtension
    static final MongoDockerExtension deploy = new MongoDockerExtension();

    @Inject
    VideosMoverMongoDatabaseService videosMoverMongoDatabaseService;

    @Test
    @DisplayName("Check that service can add to download cache")
    public void serviceCanUpdateCache() {
        String name = "name1";
        long size = 1L;
        List<TorrentFile> torrentFiles = List.of(new TorrentFile(name, size));

        videosMoverMongoDatabaseService.updateDownloadsCache(torrentFiles);

        MongoCollection<Document> collection = videosMoverMongoDatabaseService.getDownloadCache();

        assertThat("collection is empty", collection.countDocuments() == 1L);

        Document docToFind = new Document(Map.of("file_name", name, "file_size", size));

        int expectedResultsCount = 1;
        int resultsCount = 0;
        FindIterable<Document> documents = collection.find(docToFind);
        for (Document document : documents) {
            assertThat("document has wrong name", document.get("file_name").equals(name));
            assertThat("document has wrong size", document.get("file_size").equals(size));
            assertThat("document has no date", document.get("date_downloaded") != null);
            resultsCount++;
        }

        assertThat("found documents count doesn't match", expectedResultsCount == resultsCount);

        // TODO: some exception returned
//        videosMoverMongoDatabaseService.close();
    }
}
