package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.ReviewRepository
import com.simbirsoft.smoke.data.sources.ReviewPagingSource
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.IdSelectParams
import com.simbirsoft.smoke.domain.PAGE_SIZE
import com.simbirsoft.smoke.domain.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {
    private var pagingSource: ReviewPagingSource? = null
    var reviews: Flow<PagingData<Review>>? = null

    fun getReviews(hookah: Hookah) = reviews ?: run {
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100)
        ) {
            repository.provideDataSource(IdSelectParams(hookah.id)).also { pagingSource = it }
        }
            .flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .also { reviews = it }
    }

    fun reload() = viewModelScope.launch(Dispatchers.IO) {
        delay(500)
        pagingSource?.invalidate()
    }

    suspend fun addReview(review: Review) {
        repository.add(review)
    }

    class Factory(private val repository: ReviewRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                return ReviewViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}
