package com.example.nobellaureatesclient.domain.usecase

import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.domain.repository.NobelPrizesRepository
import javax.inject.Inject

class GetNobelPrizeDetailsUseCase @Inject constructor(
    private val repository: NobelPrizesRepository
) {
    suspend operator fun invoke(
        year: String,
        categoryCode: String
    ): Result<NobelPrize> = repository.getNobelPrize(year, categoryCode)
}
