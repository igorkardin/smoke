package com.simbirsoft.smoke.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simbirsoft.smoke.MainActivity

open class BaseFragment(@LayoutRes id: Int) : Fragment(id) {
    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)?.currentFragment = this
    }

    override fun onStop() {
        (activity as MainActivity?)?.currentFragment = null
        super.onStop()
    }

    fun onBackPressed(): Boolean = findNavController().navigateUp()
}