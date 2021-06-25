package com.simbirsoft.smoke.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review
import com.simbirsoft.smoke.domain.Shop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val HOOKAH_COLLECTION = "hookah"
private const val REVIEW_COLLECTION = "review"
private const val SHOPS_COLLECTION = "shops"

class HookahPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Hookah>() {
    override val mapper: (DocumentSnapshot) -> Hookah = { it.toHookah() }
    override val collectionReference: CollectionReference = Firebase.firestore.collection(HOOKAH_COLLECTION)

    fun addHookah(hookah: Hookah): Flow<Unit> = flow {
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, hookah.copy(id = newDoc.id).toMap())
            Unit
        }.await().also {
            emit(it ?: Unit)
        }
    }
}

class ReviewPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Review>() {
    override val collectionReference: CollectionReference = store.collection(REVIEW_COLLECTION)
    override val mapper: (DocumentSnapshot) -> Review = { it.toReview() }

    private val hookahCollection: CollectionReference = store.collection(HOOKAH_COLLECTION)
    fun addReviewToHookah(hookah: Hookah, review: Review): Flow<Unit> = flow {
        val hookahReference = hookahCollection.getReferenceById(hookah.id)
        val oldHookah = hookahReference.get().await()?.toHookah() ?: throw IllegalStateException()
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, review.copy(id = newDoc.id).toMap())
            val newHookah = oldHookah.updateRating(review)
            hookahReference.set(newHookah.toMap())
            Unit
        }.await().also {
            emit(it ?: Unit)
        }
    }
}

class ShopPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Shop>() {
    override val mapper: (DocumentSnapshot) -> Shop = { it.toShop() }
    override val collectionReference: CollectionReference = store.collection(SHOPS_COLLECTION)

    fun addShop(shop: Shop): Flow<Unit> = flow {
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, shop.copy(id = newDoc.id))
            Unit
        }.await().also {
            emit(it ?: Unit)
        }
    }
}

private suspend fun CollectionReference.getReferenceById(id: String) = whereEqualTo("id", id)
    .limit(1)
    .get()
    .await()
    ?.documents?.firstOrNull()?.reference
    ?: kotlin.run { throw IllegalStateException("Hookah id not found") }

fun Hookah.updateRating(newReview: Review): Hookah {
    val newAvg = (rating.average * rating.count + newReview.rating) / (rating.count + 1)
    return copy(rating = HookahRating(rating.count + 1, newAvg))
}
