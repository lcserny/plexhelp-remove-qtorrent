package net.cserny.videosmover;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.platform.commons.util.StringUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static net.cserny.videosmover.QBitTorrentV2ApiClient.SID_KEY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
                .cookie(SID_KEY, cookie)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Check that qtorrentService can delete a torrent")
    public void serviceCanDeleteTorrent() throws FileNotFoundException {
        String sid = generateOperationsCookie();

        URL torrentFileUrl = Thread.currentThread().getContextClassLoader().getResource(TORRENT_FILENAME);
        File torrentFile = new File(torrentFileUrl.getPath());

        given()
                .port(deploy.QTORRENT_PORT)
                .cookie(SID_KEY, sid)
                .contentType(ContentType.MULTIPART)
                .multiPart(new MultiPartSpecBuilder(torrentFile)
                        .fileName("ubuntu-server.iso.torrent")
                        .controlName("torrents")
                        .mimeType("application/x-bittorrent")
                        .build())
                .when()
                .post("/api/v2/torrents/add")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK);

        Response response = given()
                .port(deploy.QTORRENT_PORT)
                .cookie(SID_KEY, sid)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        List<Map<String, Object>> responseTorrents = response.jsonPath().getJsonObject("$");
        assertThat(responseTorrents.size(), equalTo(1));
        assertThat("no torrent data available", !responseTorrents.get(0).isEmpty());
        assertThat("torrent data doesn't have a hash", responseTorrents.get(0).containsKey("hash"));

        String hash = (String) responseTorrents.get(0).get("hash");
        assertThat("torrent hash is empty", StringUtils.isNotBlank(hash));

        qBitTorrentService.delete(sid, hash, true);

        Response afterDeleteResponse = given()
                .port(deploy.QTORRENT_PORT)
                .cookie(SID_KEY, sid)
                .when()
                .post("/api/v2/torrents/info")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        List<Map<String, Object>> afterDeleteList = afterDeleteResponse.jsonPath().getJsonObject("$");
        assertThat(afterDeleteList.size(), equalTo(0));
    }

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
        return response.cookie(SID_KEY);
    }
}
