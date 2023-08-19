package net.cserny.videosmover;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static net.cserny.videosmover.QBitTorrentV2ApiClient.SID_KEY;
import static net.cserny.videosmover.QTorrentTestContainer.QTORRENT_PORT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Testcontainers
public class QBitTorrentServiceTest {

    private static final String TORRENT_FILENAME = "ubuntu-server.iso.torrent";

    @Container
    public static QTorrentTestContainer torrentContainer = QTorrentTestContainer.getInstance();

    @DynamicPropertySource
    public static void qTorrentProperties(DynamicPropertyRegistry registry) {
        registry.add("torrent.webui.baseUrl", () -> "http://localhost:" + QTORRENT_PORT);
    }

    @Autowired
    QBitTorrentService qBitTorrentService;

    @Test
    @DisplayName("Check that we can communicate with QBittorrent Docker Container")
    public void qtorrentWorks() {
        String cookie = generateOperationsCookie();

        given()
                .port(QTorrentTestContainer.QTORRENT_PORT)
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
                .port(QTorrentTestContainer.QTORRENT_PORT)
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
                .port(QTorrentTestContainer.QTORRENT_PORT)
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
                .port(QTorrentTestContainer.QTORRENT_PORT)
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
        return qBitTorrentService.generateSid();
    }
}
