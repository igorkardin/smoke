package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.ui.BaseFragment

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {
    private val binding by viewBinding<FragmentHookahBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.setOnClickListener {
            findNavController().navigate(HookahFragmentDirections.toDetails())
        }
    }
}