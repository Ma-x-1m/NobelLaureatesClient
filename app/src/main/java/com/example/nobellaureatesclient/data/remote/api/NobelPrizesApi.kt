package com.example.nobellaureatesclient.data.remote.api

import com.example.nobellaureatesclient.data.remote.dto.NobelPrizesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NobelPrizesApi @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getNobelPrizes(
        year: Int? = null,
        category: String? = null,
        limit: Int = 100,
        offset: Int = 0
    ): NobelPrizesResponseDto = httpClient.get(NOBEL_PRIZES_ENDPOINT) {
        year?.let { parameter("nobelPrizeYear", it) }
        category?.takeIf { it.isNotBlank() }?.let { parameter("nobelPrizeCategory", it) }
        parameter("limit", limit)
        parameter("offset", offset)
    }.body()

    companion object {
        const val BASE_URL = "https://api.nobelprize.org/2.1/"
        private const val NOBEL_PRIZES_ENDPOINT = "${BASE_URL}nobelPrizes"
    }
}
