package com.simbirsoft.smoke.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simbirsoft.smoke.data.repositories.DiscountRepository

class DiscountViewModel(private val discountRepository: DiscountRepository) : ViewModel() {
    class Factory(private val repository: DiscountRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscountViewModel::class.java)) {
                return DiscountViewModel(repository) as T
            } else throw IllegalStateException("Bad ViewModel")
        }
    }
}