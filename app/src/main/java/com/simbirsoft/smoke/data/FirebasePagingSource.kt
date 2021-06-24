package com.simbirsoft.smoke.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.simbirsoft.smoke.domain.PAGE_SIZE
import java.io.IOException

const val EMPTY_FIREBASE_ID = "mt"

abstract class FirebasePagingSource<T : Any> : PagingSource<Query, T>() {
    protected abstract val mapper: ((DocumentSnapshot) -> T)
    protected abstract val collectionReference: CollectionReference

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, T> {
        val currentPage = params.key ?: collectionReference.pageAfter()

        val currentPageQuerySnapshot = currentPage.get().await()
        val lastSnapshot = if (currentPageQuerySnapshot?.isEmpty == false) {
            currentPageQuerySnapshot.documents.last()
        } else {
            null
        }
        val data = currentPageQuerySnapshot?.map { snapshot -> mapper(snapshot) }

        val nextPage = collectionReference.pageAfter(lastSnapshot)

        return try {
            LoadResult.Page(
                data = data!!,
                prevKey = null,
                nextKey = lastSnapshot?.let { nextPage },
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, T>): Query? = null

    private fun CollectionReference.pageAfter(
        lastSnap: DocumentSnapshot? = null, pageSize: Long = PAGE_SIZE
    ) = orderBy("id")
        .startAfter(lastSnap)
        .limit(pageSize)

}