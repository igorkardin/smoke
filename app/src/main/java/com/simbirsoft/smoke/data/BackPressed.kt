package com.simbirsoft.smoke.data

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

interface BackPressed {
    fun onBackPressed(): Boolean
}