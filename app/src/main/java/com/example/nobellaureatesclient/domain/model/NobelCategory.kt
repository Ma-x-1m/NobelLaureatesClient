package com.example.nobellaureatesclient.domain.model

enum class NobelCategory(val apiCode: String, val displayName: String) {
    ALL("", "Все категории"),
    PHYSICS("phy", "Физика"),
    CHEMISTRY("che", "Химия"),
    MEDICINE("med", "Медицина"),
    LITERATURE("lit", "Литература"),
    PEACE("pea", "Мир"),
    ECONOMICS("eco", "Экономика");

    companion object {
        fun fromApiCode(code: String?): NobelCategory =
            entries.firstOrNull { it.apiCode.equals(code, ignoreCase = true) } ?: ALL
    }
}
