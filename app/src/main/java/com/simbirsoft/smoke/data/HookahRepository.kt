package com.simbirsoft.smoke.data

import androidx.paging.PagingSource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.Review
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HookahRepository {
    private val source = HookahPagingSource(Firebase.firestore)
    private val reviewTestSource = ReviewPagingSource(Firebase.firestore)

    init {
        GlobalScope.launch {
            reviewTestSource.addReviewToHookah(
                Hookah(
                    "test_hookah", HookahRating(1, 5.0), "HookahName",
                    "https://shutniki.club/wp-content/uploads/2020/04/smeshnye_kartinki_kalyan_6_27153457.jpg",
                    10000
                ),
                review = Review(EMPTY_FIREBASE_ID, hookahId = 1, author = "Me", body = "Somebody", 4)
            ).collect {}
        }
    }

    fun getHookahs(): PagingSource<Query, Hookah> = source
}

private const val HOOKAH_COLLECTION = "hookah"
private const val REVIEW_COLLECTION = "review"

class HookahPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Hookah>() {
    override val mapper: (DocumentSnapshot) -> Hookah = { it.toHookah() }
    override val collectionReference: CollectionReference = Firebase.firestore.collection(HOOKAH_COLLECTION)
}

class ReviewPagingSource(val store: FirebaseFirestore) : FirebasePagingSource<Review>() {
    override val collectionReference: CollectionReference = store.collection(REVIEW_COLLECTION)
    override val mapper: (DocumentSnapshot) -> Review = { it.toReview() }

    private val hookahCollection: CollectionReference = store.collection(HOOKAH_COLLECTION)
    fun addReviewToHookah(hookah: Hookah, review: Review): Flow<Unit> = flow {
        val hookahReference = hookahCollection.getReferenceById(hookah.id)
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, review.copy(id = newDoc.id).toMap())
            val newHookah = hookah.updateRating(review)
            hookahReference.set(newHookah.toMap())
            Unit
        }.await().also {
            emit(it ?: Unit)
        }
    }
}

suspend fun <T> Task<T>.await() = suspendCoroutine<T?> { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resumeWith(Result.success(task.result))
        } else {
            continuation.resumeWithException(task.exception ?: IllegalStateException("Missing exception"))
        }
    }
}

private suspend fun CollectionReference.getReferenceById(id: String) = whereEqualTo("id", id)
    .limit(1)
    .get()
    .await()
    ?.documents?.firstOrNull()?.reference
    ?: kotlin.run { throw IllegalStateException("Hookah id not found") }

private fun Hookah.updateRating(newReview: Review): Hookah {
    val newAvg = (rating.average * rating.count + newReview.rating) / (rating.count + 1)
    return copy(rating = HookahRating(rating.count + 1, newAvg))
}
