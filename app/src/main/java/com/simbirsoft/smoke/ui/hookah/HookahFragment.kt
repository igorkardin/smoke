package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.BaseLoadStateAdapter
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {

    @Inject
    lateinit var viewModelFactory: HookahViewModel.Factory

    private val binding by viewBinding<FragmentHookahBinding>()
    private val viewModel by viewModels<HookahViewModel> { viewModelFactory }

    private val adapter = HookahAdapter()

    override fun inject(app: App) = app.hookahComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.apply {
            setOnClickListener {
                mainNavController.navigate(BottomNavFragmentDirections.toHookahDetails(it))
            }
        }
        binding.recycler.apply {
            adapter = this@HookahFragment.adapter.withLoadStateFooter(
                footer = BaseLoadStateAdapter { this@HookahFragment.adapter.retry() }
            )
        }
        lifecycleScope.launchWhenStarted {
            viewModel.hookahs.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}