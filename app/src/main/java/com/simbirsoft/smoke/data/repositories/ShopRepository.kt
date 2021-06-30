package com.simbirsoft.smoke.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.sources.FirebasePagingSource
import com.simbirsoft.smoke.data.sources.SHOPS_COLLECTION
import com.simbirsoft.smoke.data.sources.ShopPagingSource
import com.simbirsoft.smoke.data.toMap
import com.simbirsoft.smoke.domain.AddRepository
import com.simbirsoft.smoke.domain.Shop

class ShopRepository(private val store: FirebaseFirestore) : AddRepository<Shop, AddRepository.EmptyParams> {
    override val defaultParams: AddRepository.EmptyParams = AddRepository.EmptyParams
    private val collectionReference: CollectionReference = store.collection(SHOPS_COLLECTION)

    override fun provideDataSource(params: AddRepository.EmptyParams): FirebasePagingSource<Shop> =
        ShopPagingSource(store)

    override suspend fun add(t: Shop) {
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, t.copy(id = newDoc.id).toMap())
            Unit
        }
    }
}