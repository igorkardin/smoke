package com.simbirsoft.smoke.data

import com.google.firebase.firestore.DocumentSnapshot
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review

fun DocumentSnapshot.toHookah() = Hookah(
    id = getString("id")!!,
    name = getString("name")!!,
    picture = getString("picture")!!,
    price = getLong("price")!!.toInt(),
    rating = HookahRating(
        count = getLong("review_count")!!,
        average = getDouble("review_avg")!!
    )
)

fun Hookah.toMap() = mapOf(
    "id" to id,
    "picture" to picture,
    "name" to name,
    "price" to price,
    "count" to rating.count,
    "average" to rating.average
)

fun DocumentSnapshot.toReview() = Review(
    id = getString("id")!!,
    hookahId = getLong("hookah")!!,
    author = getString("author")!!,
    body = getString("body")!!,
    rating = getLong("rating")!!
)

fun Review.toMap() = mapOf(
    "id" to id,
    "hookah" to hookahId,
    "author" to author,
    "body" to body,
    "rating" to rating
)