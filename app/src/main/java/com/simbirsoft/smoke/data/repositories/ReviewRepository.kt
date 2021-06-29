package com.simbirsoft.smoke.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.*
import com.simbirsoft.smoke.data.sources.*
import com.simbirsoft.smoke.domain.AddRepository
import com.simbirsoft.smoke.domain.IdSelectParams
import com.simbirsoft.smoke.domain.Review

class ReviewRepository(private val store: FirebaseFirestore) :
    AddRepository<Review, IdSelectParams> {
    override val defaultParams: IdSelectParams = IdSelectParams()
    private val collectionReference: CollectionReference = store.collection(REVIEW_COLLECTION)
    private val hookahCollection: CollectionReference = store.collection(HOOKAH_COLLECTION)

    override fun provideDataSource(params: IdSelectParams): FirebasePagingSource<Review> = ReviewPagingSource(
        store, params.id
    )

    override suspend fun add(t: Review) {
        val hookahReference = hookahCollection.getReferenceById(t.hookahId)
        val oldHookah = hookahReference.get().await()?.toHookah() ?: throw IllegalStateException("No hookah")
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, t.copy(id = newDoc.id).toMap())
            val newHookah = oldHookah.updateRating(t)
            hookahReference.set(newHookah.toMap())
            Unit
        }
    }
}