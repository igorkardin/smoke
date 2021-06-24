package com.simbirsoft.smoke.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review
import com.simbirsoft.smoke.domain.Shop
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun DocumentSnapshot.toHookah() = Hookah(
    id = getString("id")!!,
    name = getString("name")!!,
    picture = getString("picture")!!,
    price = getLong("price")!!.toInt(),
    rating = HookahRating(count = getLong("review_count")!!, average = getDouble("review_avg")!!),
    description = getString("description")!!
)

fun Hookah.toMap() = mapOf(
    "id" to id,
    "picture" to picture,
    "name" to name,
    "price" to price,
    "review_count" to rating.count,
    "review_avg" to rating.average,
    "description" to description
)

fun DocumentSnapshot.toReview() = Review(
    id = getString("id")!!,
    hookahId = getString("hookah")!!,
    author = getString("author")!!,
    body = getString("body")!!,
    rating = getLong("rating")!!
)

fun Shop.toMap() = mapOf(
    "id" to id,
    "picture" to picture,
    "name" to name,
    "location" to GeoPoint(latitude, longitude)
)

fun DocumentSnapshot.toShop(): Shop {
    val geoPoint = getGeoPoint("location")!!
    return Shop(
        id = getString("id")!!,
        longitude = geoPoint.longitude,
        latitude = geoPoint.longitude,
        picture = getString("picture")!!,
        name = getString("name")!!
    )
}

fun Review.toMap() = mapOf(
    "id" to id,
    "hookah" to hookahId,
    "author" to author,
    "body" to body,
    "rating" to rating
)

suspend fun <T> Task<T>.await() = suspendCoroutine<T?> { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resumeWith(Result.success(task.result))
        } else {
            continuation.resumeWithException(task.exception ?: IllegalStateException("Missing exception"))
        }
    }
}