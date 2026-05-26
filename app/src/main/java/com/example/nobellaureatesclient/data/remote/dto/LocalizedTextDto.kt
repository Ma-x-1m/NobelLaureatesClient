package com.example.nobellaureatesclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalizedTextDto(
    @SerialName("en") val en: String? = null,
    @SerialName("se") val se: String? = null,
    @SerialName("no") val no: String? = null
) {
    fun bestEffort(): String = en ?: se ?: no ?: ""
}
