package be.genon.FileMediaMetadata

import com.drew.imaging.ImageMetadataReader
import com.drew.imaging.ImageProcessingException
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.mov.QuickTimeDirectory
import com.drew.metadata.mp4.Mp4Directory
import io.smallrye.common.annotation.Blocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jboss.logging.Logger
import java.io.File
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MediaMetadataService {
    private val LOG = Logger.getLogger(javaClass)
    val yearPattern = Regex("/\\d{4}/")

    @Blocking
    fun getReferenceDate(file: File): LocalDate? {
        LOG.debug("File ${file.name}")
        val imageMetadata = try {
            val metadata = ImageMetadataReader.readMetadata(file)
            LOG.debug(metadata
                .directories
                .flatMap { dir ->
                    dir.tags
                        .map { tag -> "${dir} / $tag" }
                }
                .joinToString(" # "))
            metadata
        } catch (e: ImageProcessingException) {
            LOG.warn("File ${file.name} has no metadata")
            null
        }

        val referenceDate = imageMetadata?.findAGoddamnDate()?.let {
            Instant.fromEpochMilliseconds(it.time).toLocalDateTime(TimeZone.UTC).date
        }
        LOG.debug("Date read ${referenceDate.toString()}")
        // If not found in metadata, try to find the year in the file path (assume /<YEAR>/ pattern)
        // and use this instead
        return referenceDate ?: yearPattern
            .find(file.path)?.value?.replace("/","")
            ?.let { LocalDate(Integer.parseInt(it), 1, 1) }
    }

    private fun Metadata.findAGoddamnDate(): Date? {
        val exifSubIF = this.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)
        if (exifSubIF != null) {
            return exifSubIF.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
                ?: exifSubIF.getDate(ExifSubIFDDirectory.TAG_DATETIME)
                ?: exifSubIF.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)
        }
        val mp4Metadata = this.getFirstDirectoryOfType(Mp4Directory::class.java)
        if (mp4Metadata != null) {
            return mp4Metadata.getDate(Mp4Directory.TAG_CREATION_TIME)
        }
        val quickTimeMetadata = this.getFirstDirectoryOfType(QuickTimeDirectory::class.java)
        if (quickTimeMetadata != null) {
            return quickTimeMetadata.getDate(QuickTimeDirectory.TAG_CREATION_TIME)
        }
        return null
    }
}
