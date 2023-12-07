package be.genon

import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

@RegisterRestClient
interface GooglePhotosAPI {
    @GET
    @Path("/v1/mediaItems")
    fun getPhotos(
        @QueryParam("pageToken") nextPageToken: String?,
        @HeaderParam("Authorization") token: String,
    ): Uni<MediaItems>
}
