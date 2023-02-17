package net.cserny.videosmover;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Testcontainers
public class QBitTorrentServiceTest {

    private static final String TORRENT_FILENAME = "ubuntu-server.iso.torrent";

    @Inject
    QBitTorrentService qBitTorrentService;

    @RegisterExtension
    static final QTorrentDockerExtension deploy = new QTorrentDockerExtension();

    @Test
    @DisplayName("Check that we can communicate with QBittorrent Docker Container")
    public void qtorrentWorks() {
        String cookie = generateOperationsCookie();

        given()
                .port(deploy.QTORRENT_PORT)
                .cookie("SID", cookie)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Check that we can add a torrent")
    public void qtorrentCanAddTorrent() throws FileNotFoundException {
        String sid = generateOperationsCookie();

        URL torrentFileUrl = Thread.currentThread().getContextClassLoader().getResource(TORRENT_FILENAME);
        File torrentFile = new File(torrentFileUrl.getPath());

        // TODO: use torrent url instead of file in test resources

//        MultipartTorrent torrent = new MultipartTorrent();
        // FIXME: error stream reset, broken pipe
//        torrent.file = new FileInputStream(torrentFile);
//        torrent.fileName = TORRENT_FILENAME;

//        qBitTorrentService.add(sid, torrent);
        qBitTorrentService.add(sid, torrentFile);

        Response response = given()
                .port(deploy.QTORRENT_PORT)
                .cookie("SID", sid)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

//        response.jsonPath().get list size more than zero
    }

    // TODO: add actual test that adds a torrent and uses QBitTorrentService to remove it

    private String generateOperationsCookie() {
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
        return response.cookie("SID");
    }
}
