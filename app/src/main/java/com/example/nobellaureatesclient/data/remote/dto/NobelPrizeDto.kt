package com.example.nobellaureatesclient.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizesResponseDto(
    @SerialName("nobelPrizes") val nobelPrizes: List<NobelPrizeDto> = emptyList(),
    @SerialName("meta") val meta: MetaDto? = null
)

@Serializable
data class MetaDto(
    @SerialName("offset") val offset: Int? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("nobelPrizeYear") val nobelPrizeYear: Int? = null,
    @SerialName("yearTo") val yearTo: Int? = null,
    @SerialName("count") val count: Int? = null
)

@Serializable
data class NobelPrizeDto(
    @SerialName("awardYear") val awardYear: String? = null,
    @SerialName("category") val category: LocalizedTextDto? = null,
    @SerialName("categoryFullName") val categoryFullName: LocalizedTextDto? = null,
    @SerialName("dateAwarded") val dateAwarded: String? = null,
    @SerialName("prizeAmount") val prizeAmount: Long? = null,
    @SerialName("prizeAmountAdjusted") val prizeAmountAdjusted: Long? = null,
    @SerialName("laureates") val laureates: List<LaureateDto>? = null
)
