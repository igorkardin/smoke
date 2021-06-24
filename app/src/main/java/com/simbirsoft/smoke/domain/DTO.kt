package com.simbirsoft.smoke.domain

data class Hookah(
    override val id: String,
    val rating: HookahRating,
    val name: String,
    val picture: String,
    val price: Int
) : DTO

data class HookahRating(val count: Long, val average: Double)
data class Review(
    override val id: String,
    val hookahId: Long,
    val author: String,
    val body: String,
    val rating: Long
) : DTO

data class Shop(
    override val id: String,
    val picture: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
) : DTO

interface DTO {
    val id: String
}

const val PAGE_SIZE = 10L
