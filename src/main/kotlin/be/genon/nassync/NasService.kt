package be.genon.nassync

import be.genon.FileMediaMetadata.MediaMetadataService
import be.genon.StandardMedia
import io.smallrye.mutiny.Multi
import org.jboss.logging.Logger
import java.io.File
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import kotlin.streams.asStream

@Path("nasphotos")
class NasService {
    private val LOG = Logger.getLogger(javaClass)

    @Inject
    @field:Default
    lateinit var mediaMetadataService: MediaMetadataService

    @GET
    fun getMediasFromNas(): Multi<StandardMedia> {
        return Multi.createFrom().items {
            File("/Volumes/photo/2004")
                .walk()
                .filterNot { it.isDirectory }
                .map  {
                    val referenceDate = mediaMetadataService.getReferenceDate(it)
                    if(referenceDate==null){
                        LOG.warn("${it.name}: could not get a reference time")
                    }
                    StandardMedia(it.name, referenceDate,it.path)
                }
                .asStream()
        }
    }
}
