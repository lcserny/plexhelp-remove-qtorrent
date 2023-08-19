package net.cserny.videosmover;

import lombok.AllArgsConstructor;
import net.cserny.videosmover.TorrentFile.TorrentFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
public class QBitTorrentV2ApiClient {

    public static final String SID_KEY = "SID";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TorrentWebUIConfiguration configuration;

    public ResponseEntity<String> login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.exchange(configuration.getBaseUrl() + "/api/v2/auth/login", HttpMethod.POST, request, String.class);
    }

    public ResponseEntity<String> delete(String sid, String hash, boolean deleteFiles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.COOKIE, String.format("%s=%s", SID_KEY, sid));

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("hashes", hash);
        map.add("deleteFiles", String.valueOf(deleteFiles));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.exchange(configuration.getBaseUrl() + "/api/v2/torrents/delete", HttpMethod.POST, request, String.class);
    }

    ResponseEntity<TorrentFiles> files(String sid, String hash) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.COOKIE, String.format("%s=%s", SID_KEY, sid));

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("hash", hash);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.exchange(configuration.getBaseUrl() + "/api/v2/torrents/files", HttpMethod.POST, request, TorrentFiles.class);
    }
}
