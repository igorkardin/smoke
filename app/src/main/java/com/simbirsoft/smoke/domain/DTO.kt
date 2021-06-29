package com.simbirsoft.smoke.domain

import com.simbirsoft.smoke.data.sources.EMPTY_FIREBASE_ID

data class Hookah(
    override val id: String = EMPTY_FIREBASE_ID,
    val rating: HookahRating,
    val name: String,
    val picture: String,
    val price: Int,
    val description: String
) : DTO

data class HookahRating(val count: Long, val average: Double)

data class Review(
    override val id: String = EMPTY_FIREBASE_ID,
    val hookahId: String,
    val author: String,
    val body: String,
    val rating: Long
) : DTO

data class Shop(
    override val id: String = EMPTY_FIREBASE_ID,
    val picture: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
) : DTO

data class Discount(
    override val id: String,
    val shopId: String,
    val name: String,
    val description: String
) : DTO

interface DTO {
    val id: String
}

const val PAGE_SIZE = 7L

interface DataSourceParams
