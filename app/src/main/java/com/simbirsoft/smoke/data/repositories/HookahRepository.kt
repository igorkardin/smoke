package com.simbirsoft.smoke.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.simbirsoft.smoke.data.sources.HOOKAH_COLLECTION
import com.simbirsoft.smoke.data.sources.HookahPagingSource
import com.simbirsoft.smoke.data.toMap
import com.simbirsoft.smoke.domain.AddRepository
import com.simbirsoft.smoke.domain.Hookah

class HookahRepository(private val store: FirebaseFirestore) : AddRepository<Hookah, AddRepository.EmptyParams> {
    override val defaultParams = AddRepository.EmptyParams
    private val collectionReference = store.collection(HOOKAH_COLLECTION)

    override fun provideDataSource(params: AddRepository.EmptyParams) = HookahPagingSource(store)

    override suspend fun add(t: Hookah) {
        store.runTransaction { transaction ->
            val newDoc = collectionReference.document()
            transaction.set(newDoc, t.copy(id = newDoc.id).toMap())
            Unit
        }
    }
}