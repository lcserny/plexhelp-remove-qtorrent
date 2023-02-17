package net.cserny.videosmover;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

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
