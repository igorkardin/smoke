package com.simbirsoft.smoke.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.simbirsoft.smoke.data.await
import com.simbirsoft.smoke.domain.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val EMPTY_FIREBASE_ID = "mt"

abstract class ChildPagingSource<T : Any>(private val selectId: String?) : FirebasePagingSource<T>() {
    abstract val parentField: String
    override fun Query.applySelectOptions() = selectId?.let { whereEqualTo(parentField, it) } ?: this
}

abstract class FirebasePagingSource<T : Any> : PagingSource<Query, T>() {
    protected abstract val mapper: ((DocumentSnapshot) -> T)
    protected abstract val collectionReference: CollectionReference

    protected open fun Query.applySelectOptions(): Query = this

    final override suspend fun load(params: LoadParams<Query>): LoadResult<Query, T> = withContext(Dispatchers.IO) {
        return@withContext try {
            val currentPage = params.key ?: collectionReference.pageAfter()
            val currentPageQuerySnapshot = currentPage.get().await()
            val lastSnapshot = if (currentPageQuerySnapshot?.isEmpty == false) {
                currentPageQuerySnapshot.documents.last()
            } else {
                null
            }
            val data = currentPageQuerySnapshot?.map { snapshot -> mapper(snapshot) }
            val nextPage = collectionReference.pageAfter(lastSnapshot)
            if (currentPageQuerySnapshot?.isEmpty == true && currentPageQuerySnapshot.metadata.isFromCache) {
                throw IllegalStateException("BEBEBE")
            }
            LoadResult.Page(
                data = data!!,
                prevKey = null,
                nextKey = lastSnapshot?.let { nextPage },
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, T>): Query? = null

    private fun CollectionReference.pageAfter(lastSnap: DocumentSnapshot? = null, pageSize: Long = PAGE_SIZE) =
        applySelectOptions()
            .orderBy("id")
            .startAfter(lastSnap?.id)
            .limit(pageSize)
}