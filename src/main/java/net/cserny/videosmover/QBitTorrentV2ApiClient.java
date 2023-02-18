package net.cserny.videosmover;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/v2")
@RegisterRestClient
public interface QBitTorrentV2ApiClient {

    String SID_KEY = "SID";

    @POST
    @Path("/auth/login")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    Response login(@FormParam("username") String username, @FormParam("password") String password);

    @POST
    @Path("/torrents/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    String delete(@CookieParam(SID_KEY) String sid, @FormParam("hashes") String hash, @FormParam("deleteFiles") boolean deleteFiles);

    @POST
    @Path("/torrents/files")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    List<TorrentFile> files(@CookieParam(SID_KEY) String sid, @FormParam("hash") String hash);

    @ClientExceptionMapper
    static QBitTorrentException handleException(Response response) {
        if (response.getStatus() != 200) {
            throw new QBitTorrentException("HTTP " + response.getStatus() + ": " + response.readEntity(String.class));
        }
        return null;
    }

    class QBitTorrentException extends RuntimeException {

        public QBitTorrentException(String message) {
            super(message);
        }
    }
}
