package com.example.nobellaureatesclient.domain.repository

import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize

interface NobelPrizesRepository {

    suspend fun getNobelPrizes(
        year: Int?,
        category: NobelCategory
    ): Result<List<NobelPrize>>

    suspend fun getNobelPrize(
        year: String,
        categoryCode: String
    ): Result<NobelPrize>
}
