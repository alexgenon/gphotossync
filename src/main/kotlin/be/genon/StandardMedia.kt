package be.genon

import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class StandardMedia(val filename: String, val creationTime: LocalDate?,val path:String?=null)
