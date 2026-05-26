package com.example.nobellaureatesclient.data.repository

import com.example.nobellaureatesclient.data.mapper.toDomain
import com.example.nobellaureatesclient.data.remote.api.NobelPrizesApi
import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.domain.repository.NobelPrizesRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NobelPrizesRepositoryImpl @Inject constructor(
    private val api: NobelPrizesApi
) : NobelPrizesRepository {

    override suspend fun getNobelPrizes(
        year: Int?,
        category: NobelCategory
    ): Result<List<NobelPrize>> = runCatchingApi {
        val categoryCode = category.apiCode.ifBlank { null }
        api.getNobelPrizes(year = year, category = categoryCode)
            .nobelPrizes
            .map { it.toDomain() }
            .sortedByDescending { it.awardYear }
    }

    override suspend fun getNobelPrize(
        year: String,
        categoryCode: String
    ): Result<NobelPrize> = runCatchingApi {
        val parsedYear = year.toIntOrNull()
        val category = categoryCode.ifBlank { null }
        val prize = api.getNobelPrizes(year = parsedYear, category = category)
            .nobelPrizes
            .firstOrNull { dto ->
                dto.awardYear == year &&
                    (category == null || dto.category?.bestEffort()?.equals(category, ignoreCase = true) == true)
            }
            ?: error("Премия за $year (${categoryCode.ifBlank { "—" }}) не найдена")
        prize.toDomain()
    }

    private inline fun <T> runCatchingApi(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (ce: CancellationException) {
        throw ce
    } catch (t: Throwable) {
        Result.failure(t)
    }
}
