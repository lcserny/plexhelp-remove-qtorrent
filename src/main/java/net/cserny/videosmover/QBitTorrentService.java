package net.cserny.videosmover;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class QBitTorrentService implements TorrentService {

    @RestClient
    QBitTorrentV2ApiClient client;

    @Override
    public void delete(String hash, boolean deleteFiles) {
        client.delete(hash, deleteFiles);
    }

    @Override
    public List<TorrentFile> listFiles(String hash) {
        return client.files(hash);
    }
}
