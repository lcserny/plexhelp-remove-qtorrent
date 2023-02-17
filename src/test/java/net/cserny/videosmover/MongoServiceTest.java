package net.cserny.videosmover;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@QuarkusTest
@Testcontainers
public class MongoServiceTest {

    @RegisterExtension
    static final MongoDockerExtension deploy = new MongoDockerExtension();

    @Test
    @DisplayName("Check that ...")
    public void mongoWorks() {
        // TODO
    }
}
