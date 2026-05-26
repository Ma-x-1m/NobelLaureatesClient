package com.example.nobellaureatesclient.domain.usecase

import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.domain.repository.NobelPrizesRepository
import javax.inject.Inject

class GetNobelPrizesUseCase @Inject constructor(
    private val repository: NobelPrizesRepository
) {
    suspend operator fun invoke(
        year: Int?,
        category: NobelCategory
    ): Result<List<NobelPrize>> = repository.getNobelPrizes(year, category)
}
