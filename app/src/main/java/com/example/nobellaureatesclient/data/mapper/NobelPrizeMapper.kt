package com.example.nobellaureatesclient.data.mapper

import com.example.nobellaureatesclient.data.remote.dto.LaureateDto
import com.example.nobellaureatesclient.data.remote.dto.NobelPrizeDto
import com.example.nobellaureatesclient.domain.model.Laureate
import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize

private const val NOBEL_PRIZE_ORG = "https://www.nobelprize.org"

fun NobelPrizeDto.toDomain(): NobelPrize {
    val categoryCode = category?.bestEffort()?.lowercase().orEmpty()
    return NobelPrize(
        awardYear = awardYear.orEmpty(),
        category = NobelCategory.fromApiCode(categoryCode),
        categoryFullName = categoryFullName?.bestEffort().orEmpty(),
        laureates = laureates?.map { it.toDomain() }.orEmpty(),
        prizeAmount = prizeAmount,
        dateAwarded = dateAwarded
    )
}

fun LaureateDto.toDomain(): Laureate {
    val name = fullName?.bestEffort()
        ?: knownName?.bestEffort()
        ?: orgName?.bestEffort()
        ?: "Unknown"
    val country = birth?.place?.countryNow?.bestEffort()
        ?: birth?.place?.country?.bestEffort()
    val portrait = id?.let { "$NOBEL_PRIZE_ORG/images/$it.jpg" }
    return Laureate(
        id = id.orEmpty(),
        fullName = name,
        motivation = motivation?.bestEffort().orEmpty(),
        country = country,
        portraitUrl = portrait,
        birthDate = birth?.date,
        deathDate = death?.date
    )
}
