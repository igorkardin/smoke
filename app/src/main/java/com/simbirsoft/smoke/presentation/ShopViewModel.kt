package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.ShopRepository
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.PAGE_SIZE
import com.simbirsoft.smoke.domain.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ShopViewModel(private val shopRepository: ShopRepository) : ViewModel() {
    val shops: Flow<PagingData<Shop>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100)
    ) { shopRepository.provideDataSource() }
        .flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    class Factory(private val repository: ShopRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
                return ShopViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}
