package com.example.nobellaureatesclient.domain.model

data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String,
    val country: String?,
    val portraitUrl: String?,
    val birthDate: String?,
    val deathDate: String?
)
