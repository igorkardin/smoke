package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.HookahRepository
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.domain.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class HookahViewModel : ViewModel() {
    private val repository = HookahRepository()
    val hookahs: Flow<PagingData<Hookah>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100,
        )
    ) {
        repository.getHookahs()
    }.flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)
}