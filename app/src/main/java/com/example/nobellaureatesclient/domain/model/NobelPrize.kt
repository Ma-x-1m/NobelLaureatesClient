package com.example.nobellaureatesclient.domain.model

data class NobelPrize(
    val awardYear: String,
    val category: NobelCategory,
    val categoryFullName: String,
    val laureates: List<Laureate>,
    val prizeAmount: Long?,
    val dateAwarded: String?
) {
    val id: String get() = "$awardYear-${category.apiCode}"
}
