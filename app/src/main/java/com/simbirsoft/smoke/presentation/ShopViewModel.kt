package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simbirsoft.smoke.data.repositories.ShopRepository

class ShopViewModel(private val shopRepository: ShopRepository) : ViewModel() {
    class Factory(private val repository: ShopRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                return ShopViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}
