package com.simbirsoft.smoke.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.*
import com.simbirsoft.smoke.data.sources.DISCOUNT_COLLECTION
import com.simbirsoft.smoke.data.sources.DiscountPagingSource
import com.simbirsoft.smoke.data.sources.FirebasePagingSource
import com.simbirsoft.smoke.data.sources.SHOPS_COLLECTION
import com.simbirsoft.smoke.domain.AddRepository
import com.simbirsoft.smoke.domain.Discount
import com.simbirsoft.smoke.domain.IdSelectParams

class DiscountRepository(private val store: FirebaseFirestore) : AddRepository<Discount, IdSelectParams> {
    override val defaultParams: IdSelectParams = IdSelectParams()
    private val collectionReference: CollectionReference = store.collection(DISCOUNT_COLLECTION)
    private val hookahCollection: CollectionReference = store.collection(SHOPS_COLLECTION)

    override fun provideDataSource(params: IdSelectParams): FirebasePagingSource<Discount> = DiscountPagingSource(store)

    override suspend fun add(t: Discount) {
        val shopReference = hookahCollection.getReferenceById(t.shopId)
        shopReference.get().await()?.toShop() ?: throw IllegalStateException("No shop")
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, t.copy(id = newDoc.id).toMap())
            Unit
        }
    }
}