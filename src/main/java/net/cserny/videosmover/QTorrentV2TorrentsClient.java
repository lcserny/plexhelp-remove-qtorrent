package net.cserny.videosmover;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v2/torrents")
@RegisterRestClient
public interface QTorrentV2TorrentsClient {

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    String delete(@FormParam("hashes") String hash, @FormParam("deleteFiles") boolean deleteFiles);

    @ClientExceptionMapper
    static QTorrentException handleException(Response response) {
        if (response.getStatus() != 200) {
            throw new QTorrentException("HTTP " + response.getStatus() + ": " + response.readEntity(String.class));
        }
        return null;
    }

    class QTorrentException extends RuntimeException {

        public QTorrentException(String message) {
            super(message);
        }
    }
}
