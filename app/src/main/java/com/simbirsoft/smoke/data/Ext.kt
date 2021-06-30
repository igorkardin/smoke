package com.simbirsoft.smoke.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.simbirsoft.smoke.domain.*
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

fun Shop.toMap() = mapOf(
    "id" to id,
    "picture" to picture,
    "name" to name,
    "location" to GeoPoint(latitude, longitude)
)

fun DocumentSnapshot.toShop() = Shop(
    id = getString("id")!!,
    longitude = getDouble("longitude")!!,
    latitude = getDouble("latitude")!!,
    picture = getString("picture")!!,
    name = getString("name")!!
)

fun Review.toMap() = mapOf(
    "id" to id,
    "hookah" to hookahId,
    "author" to author,
    "body" to body,
    "rating" to rating
)

fun DocumentSnapshot.toReview() = Review(
    id = getString("id")!!,
    hookahId = getString("hookah")!!,
    author = getString("author")!!,
    body = getString("body")!!,
    rating = getLong("rating")!!
)

fun Discount.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "description" to description,
    "shop" to shopId,
)

fun DocumentSnapshot.toDiscount() = Discount(
    id = getString("id")!!,
    shopId = getString("shop")!!,
    name = getString("name")!!,
    description = getString("description")!!
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

suspend fun CollectionReference.getReferenceById(id: String) = whereEqualTo("id", id)
    .limit(1)
    .get()
    .await()
    ?.documents?.firstOrNull()?.reference
    ?: run { throw IllegalStateException("Hookah id not found") }