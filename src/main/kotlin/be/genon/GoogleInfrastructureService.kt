package be.genon

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.subscription.MultiEmitter
import kotlinx.serialization.json.jsonObject
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import java.util.concurrent.atomic.AtomicInteger
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/photos")
class GoogleInfrastructureService {
    private val LOG = Logger.getLogger(javaClass)

    var code: String = ""
    private val callCounter = AtomicInteger(0)

    @RestClient
    lateinit var googleAuthAPI: GoogleAuthAPI

    @RestClient
    lateinit var googlePhotosAPI: GooglePhotosAPI

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listPhotos(@QueryParam("code") code: String): Multi<GoogleMedia> {
        this.code = code
        this.callCounter.set(0)
        val tokenUni = getAuthToken().onFailure()
            .invoke { error -> LOG.error("Error with call: $error") }
        return tokenUni.toMulti()
            .flatMap { token ->
                getPhotos(token)
            }
    }

    fun getAuthToken(): Uni<String> {
        val tokenResponseJson = googleAuthAPI.getToken(
            code,
            "obfuscated",
            "obfuscated",
            "http://localhost:8080/photos",
            "authorization_code"
        )

        return tokenResponseJson
            .invoke { response -> LOG.debug("Auth token received $response") }
            .map { (it.jsonObject)["access_token"]!!.toString() }
    }

    fun getPhotos(token: String): Multi<GoogleMedia> {
        return Multi.createFrom()
            .emitter { emitter ->
                getPagePhotos(token, null, emitter)
            }
    }

    fun getPagePhotos(token: String, nextPageToken: String?, emitter: MultiEmitter<in GoogleMedia>) {
        val counter = this.callCounter.incrementAndGet()
        googlePhotosAPI
            .getPhotos(nextPageToken, "Bearer $token")
            .onFailure()
            .invoke { error -> LOG.error("Error with call #$counter with nextPageToken = $nextPageToken: $error") }
            .subscribe()
            .with { result ->
                result.mediaItems.forEach { emitter.emit(it) }
                if (result.nextPageToken != null) {
                    getPagePhotos(token, result.nextPageToken, emitter)
                } else {
                    emitter.complete()
                }
            }
    }
}
