package com.simbirsoft.smoke.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.HookahRating
import com.simbirsoft.smoke.domain.PAGE_SIZE
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

class HookahRepository {
    private val source = HookahPagingSource()
    fun getHookahs(): PagingSource<Int, Hookah> = source
}

class HookahPagingSource : PagingSource<Int, Hookah>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hookah> {
        val nextPage = params.key ?: 1
        val data = FirebaseProvider.hookahDatabase.pageById(nextPage) { it.toHookah() }
        return try {
            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (data.isEmpty()) null else nextPage + 1,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Hookah>): Int? = state.anchorPosition
}

private fun DocumentSnapshot.toHookah() = Hookah(
    id = getLong("id")!!,
    name = getString("name")!!,
    picture = getString("picture")!!,
    price = getLong("price")!!.toInt(),
    rating = HookahRating(
        count = getLong("review_count")!!,
        average = getDouble("review_avg")!!
    )
)

fun recountAvg(previousAvg: Double, previousCount: Long, newReview: Long) =
    (previousAvg * previousCount + newReview) /
            (previousCount + 1)

suspend fun <T> CollectionReference.pageById(page: Int, pageSize: Long = PAGE_SIZE, mapper: (DocumentSnapshot) -> T) =
    suspendCoroutine<List<T>> { continuation ->
        limit(PAGE_SIZE)
            .orderBy("id")
            .startAfter((page - 1) * pageSize)
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    continuation.resumeWith(
                        Result.failure(task.exception ?: IllegalStateException("Task failed"))
                    )
                    return@addOnCompleteListener
                }
                task.result?.documents?.map { snapshot -> mapper(snapshot) }
                    ?.also { continuation.resumeWith(Result.success(it)) }
                    ?: run { continuation.resumeWith(Result.failure(IllegalStateException("No hookah"))) }
            }
    }