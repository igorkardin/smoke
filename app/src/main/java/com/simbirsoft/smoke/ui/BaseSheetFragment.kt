package com.simbirsoft.smoke.ui

import android.content.Context
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.MainActivity
import com.simbirsoft.smoke.R

abstract class BaseSheetFragment : BottomSheetDialogFragment(), BackPressed {
    val mainNavController get() = (requireActivity() as MainActivity).findNavController(R.id.main_container)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(context.applicationContext as App)
    }

    protected open fun inject(app: App) = Unit

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.currentFragment = this
    }

    override fun onPause() {
        (activity as MainActivity?)?.currentFragment = null
        super.onPause()
    }

    override fun onBackPressed(): Boolean = findNavController().navigateUp()
}