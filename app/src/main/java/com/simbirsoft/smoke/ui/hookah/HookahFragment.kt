package com.simbirsoft.smoke.ui.hookah

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {

    @Inject
    lateinit var viewModelFactory: HookahViewModel.Factory

    private val binding by viewBinding<FragmentHookahBinding>()
    private val viewModel by viewModels<HookahViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).hookahComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HookahAdapter().apply {
            setOnClickListener {
                findNavController().navigate(HookahFragmentDirections.toDetails())
            }
        }
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.hookahs.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}