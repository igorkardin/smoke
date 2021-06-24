package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.HookahPagingSource
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class HookahViewModel(private val hookahSource: HookahPagingSource) : ViewModel() {
    val hookahs: Flow<PagingData<Hookah>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100,
        )
    ) { hookahSource }
        .flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    class Factory(private val hookahSource: HookahPagingSource) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HookahViewModel::class.java)) {
                return HookahViewModel(hookahSource) as T
            } else throw IllegalStateException("Bad Viewmodel")
        }
    }
}