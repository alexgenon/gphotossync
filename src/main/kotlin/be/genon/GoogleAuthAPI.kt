package be.genon

import io.smallrye.mutiny.Uni
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RegisterRestClient
interface GoogleAuthAPI {
    @Path("token")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    fun getToken(
        @FormParam("code") code: String,
        @FormParam("client_id") client: String,
        @FormParam("client_secret") clientSecret: String,
        @FormParam("redirect_uri") redirectURI: String,
        @FormParam("grant_type") grantType: String,
    ): Uni<JsonElement>
}
