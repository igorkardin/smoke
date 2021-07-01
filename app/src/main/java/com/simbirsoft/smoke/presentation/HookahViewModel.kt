package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.HookahRepository
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HookahViewModel(private val repository: HookahRepository) : ViewModel() {
    val hookahs: Flow<PagingData<Hookah>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100)
    ) { repository.provideDataSource() }
        .flow
        .map {
            println(it)
            it
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    class Factory(private val repository: HookahRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HookahViewModel::class.java)) {
                return HookahViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}