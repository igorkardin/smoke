package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.simbirsoft.smoke.data.repositories.VideoRepository
import com.simbirsoft.smoke.domain.PAGE_SIZE
import com.simbirsoft.smoke.domain.Shop
import com.simbirsoft.smoke.domain.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {
    val videos: Flow<PagingData<Video>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE.toInt(), enablePlaceholders = true, maxSize = 100)
    ) { repository.provideDataSource() }
        .flow
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    class Factory(private val repository: VideoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
                return VideoViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}