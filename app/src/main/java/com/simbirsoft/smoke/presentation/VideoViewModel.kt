package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simbirsoft.smoke.data.repositories.VideoRepository

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {
    class Factory(private val repository: VideoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
                return VideoViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}