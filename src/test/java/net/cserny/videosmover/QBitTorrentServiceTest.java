package net.cserny.videosmover;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@Testcontainers
public class QBitTorrentServiceTest {

    @RegisterExtension
    static final QTorrentDockerExtension deploy = new QTorrentDockerExtension();

    @Test
    @DisplayName("Check that we can communicate with QBittorrent Docker Container")
    public void qtorrentWorks() {
        Response response = given()
                .port(deploy.QTORRENT_PORT)
                .formParam("username", "admin")
                .formParam("password", "adminadmin")
                .when()
                .post("/api/v2/auth/login")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        String cookie = response.cookie("SID");

        given()
                .port(deploy.QTORRENT_PORT)
                .cookie("SID", cookie)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK);
    }

    // TODO: add actual test that adds a torrent and uses QBitTorrentService to remove it
}
