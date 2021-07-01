package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahBinding
import com.simbirsoft.smoke.presentation.HookahViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.BaseLoadStateAdapter
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HookahFragment : BaseFragment(R.layout.fragment_hookah) {

    @Inject
    lateinit var viewModelFactory: HookahViewModel.Factory

    private lateinit var binding: FragmentHookahBinding
    private val viewModel by viewModels<HookahViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHookahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun inject(app: App) = app.hookahComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hookahAdapter = HookahAdapter().apply {
            setOnClickListener {
                mainNavController.navigate(BottomNavFragmentDirections.toHookahDetails(it))
            }
        }
        binding.recycler.adapter = hookahAdapter.withLoadStateFooter(
            footer = BaseLoadStateAdapter { hookahAdapter.retry() }
        )
        binding.errorState.retry.setOnClickListener {
            hookahAdapter.retry()
        }
        lifecycleScope.launch {
            hookahAdapter.loadStateFlow.collect {
                binding.errorState.root.isVisible = it.refresh is LoadState.Error
                if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                    binding.emptyState.isVisible = hookahAdapter.itemCount < 1
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.hookahs.collectLatest { hookahAdapter.submitData(it) }
        }
    }
}