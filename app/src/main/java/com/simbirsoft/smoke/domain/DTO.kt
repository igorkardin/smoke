package com.simbirsoft.smoke.domain

data class Hookah(val id: Long, val rating: HookahRating, val name: String, val picture: String, val price: Int)
data class HookahRating(val count: Long, val average: Double)

data class Review(val hookah: Hookah, val author: String, val body: String, val rating: Int)

data class Shop(val id: Long, val picture: String, val name: String, val longitude: Double, val latitude: Double)

const val PAGE_SIZE = 10L
