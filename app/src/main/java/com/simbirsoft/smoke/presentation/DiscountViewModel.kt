package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.DiscountRepository
import com.simbirsoft.smoke.domain.Discount
import com.simbirsoft.smoke.domain.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DiscountViewModel(private val discountRepository: DiscountRepository) : ViewModel() {
    val promotions: Flow<PagingData<Discount>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE.toInt(),
            enablePlaceholders = true,
            maxSize = 100
        )
    ) { discountRepository.provideDataSource() }
        .flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    class Factory(private val repository: DiscountRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscountViewModel::class.java)) {
                return DiscountViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}