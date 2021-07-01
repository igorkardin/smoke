package com.simbirsoft.smoke.domain

import com.simbirsoft.smoke.data.sources.EMPTY_FIREBASE_ID
import java.io.Serializable

data class Hookah(
    override val id: String = EMPTY_FIREBASE_ID,
    val rating: HookahRating,
    val name: String,
    val picture: String,
    val price: Int,
    val description: String
) : DTO, Serializable

data class HookahRating(val count: Long, val average: Double) : Serializable

data class Review(
    override val id: String = EMPTY_FIREBASE_ID,
    val hookahId: String,
    val author: String,
    val body: String,
    val rating: Long
) : DTO, Serializable

data class Shop(
    override val id: String = EMPTY_FIREBASE_ID,
    val picture: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
) : DTO, Serializable

data class Discount(
    override val id: String,
    val shopId: String,
    val name: String,
    val description: String
) : DTO, Serializable

data class Video(
    override val id: String,
    val length: String,
    val title: String,
    val url: String,
    val preview: String
) : DTO, Serializable

interface DTO {
    val id: String
}

const val PAGE_SIZE = 7L

interface DataSourceParams
