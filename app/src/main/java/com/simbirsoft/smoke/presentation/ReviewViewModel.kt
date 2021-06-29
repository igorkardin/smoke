package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simbirsoft.smoke.data.repositories.ReviewRepository

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {
    class Factory(private val repository: ReviewRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                return ReviewViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}
