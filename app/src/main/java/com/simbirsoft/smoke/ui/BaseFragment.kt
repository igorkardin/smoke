package com.simbirsoft.smoke.ui

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.MainActivity
import com.simbirsoft.smoke.R

open class BaseFragment(@LayoutRes id: Int) : Fragment(id) {
    val mainNavController get() = (requireActivity() as MainActivity).findNavController(R.id.main_container)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(context.applicationContext as App)
    }

    protected open fun inject(app: App) {
        /* no-op */
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.currentFragment = this
    }

    override fun onPause() {
        (activity as MainActivity?)?.currentFragment = null
        super.onPause()
    }

    fun onBackPressed(): Boolean = findNavController().navigateUp()
}