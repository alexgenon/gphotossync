package be.genon

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonElement

@kotlinx.serialization.Serializable
data class MediaItems(
    val mediaItems: List<GoogleMedia>,
    val nextPageToken: String?=null,
)

@kotlinx.serialization.Serializable
data class GoogleMedia(
    val id: String,
    val description: String?=null,
    val productUrl: String,
    val baseUrl: String,
    val mimeType: String,
    val mediaMetadata: MediaMetadata,
    val filename: String
){
    fun toStandardMedia() = StandardMedia(filename,mediaMetadata.creationTime.toLocalDateTime(TimeZone.UTC).date)
}

@kotlinx.serialization.Serializable
data class MediaMetadata(
    val creationTime: Instant,
    val width: String,
    val height: String,
    val video: JsonElement? = null,
    val photo: JsonElement? = null,
)
