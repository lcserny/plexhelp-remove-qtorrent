package net.cserny.videosmover;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;

// TODO: try this instead of deployment extension
// https://www.morling.dev/blog/quarkus-and-testcontainers/
// https://quarkus.io/guides/getting-started-testing step 13
@Testcontainers
public class HealthTest {

    @RegisterExtension
    static final FullDeploymentExtension deploy = new FullDeploymentExtension();

    @Test
    public void containersStarting() {
        given().port(deploy.QTORRENT_PORT)
                .when()
                    .post("/api/v2/auth/login", Map.of("username", "admin", "password", "adminadmin"))
                .then()
                    .statusCode(HttpStatus.SC_OK);
    }
}
