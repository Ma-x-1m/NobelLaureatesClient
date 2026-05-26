package com.example.nobellaureatesclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaureateDto(
    @SerialName("id") val id: String? = null,
    @SerialName("knownName") val knownName: LocalizedTextDto? = null,
    @SerialName("fullName") val fullName: LocalizedTextDto? = null,
    @SerialName("orgName") val orgName: LocalizedTextDto? = null,
    @SerialName("portion") val portion: String? = null,
    @SerialName("motivation") val motivation: LocalizedTextDto? = null,
    @SerialName("birth") val birth: BirthDeathDto? = null,
    @SerialName("death") val death: BirthDeathDto? = null,
    @SerialName("founded") val founded: BirthDeathDto? = null,
    @SerialName("wikipedia") val wikipedia: WikipediaDto? = null,
    @SerialName("sameAs") val sameAs: List<String>? = null
)

@Serializable
data class BirthDeathDto(
    @SerialName("date") val date: String? = null,
    @SerialName("place") val place: PlaceDto? = null
)

@Serializable
data class PlaceDto(
    @SerialName("city") val city: LocalizedTextDto? = null,
    @SerialName("country") val country: LocalizedTextDto? = null,
    @SerialName("citynow") val cityNow: LocalizedTextDto? = null,
    @SerialName("countryNow") val countryNow: LocalizedTextDto? = null
)

@Serializable
data class WikipediaDto(
    @SerialName("slug") val slug: String? = null,
    @SerialName("english") val english: String? = null
)
