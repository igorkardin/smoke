package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.ReviewRepository
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.IdSelectParams
import com.simbirsoft.smoke.domain.PAGE_SIZE
import com.simbirsoft.smoke.domain.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {

    var reviews: Flow<PagingData<Review>>? = null

    fun getReviews(hookah: Hookah) = reviews ?: run {
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100)
        ) {
            repository.provideDataSource(IdSelectParams(hookah.id))
        }
            .flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .also { reviews = it }
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
