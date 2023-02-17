package net.cserny.videosmover;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(HealthTest.class);

    @RegisterExtension
    static final FullDeploymentExtension deploy = new FullDeploymentExtension();

    // FIXME: gives 400 not 200...
    @Test
    public void containersStarting() {
        Response response = given()
                .port(deploy.QTORRENT_PORT)
                .header("Referer", "http://localhost:" + deploy.QTORRENT_PORT)
                .body(Map.of("username", "admin", "password", "adminadmin"))
                .when()
                .post("/api/v2/auth/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        LOGGER.info("LEO: " + response.prettyPrint());
    }
}
