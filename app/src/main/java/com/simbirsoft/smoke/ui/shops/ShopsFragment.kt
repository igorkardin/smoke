package com.simbirsoft.smoke.ui.shops

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentShopsBinding
import com.simbirsoft.smoke.presentation.ShopViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import com.simbirsoft.smoke.ui.openMap
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class ShopsFragment : BaseFragment(R.layout.fragment_shops) {
    @Inject
    lateinit var viewModelFactory: ShopViewModel.Factory

    private val binding by viewBinding<FragmentShopsBinding>()
    private val viewModel by viewModels<ShopViewModel> { viewModelFactory }

    override fun inject(app: App) = app.shopComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ShopsAdapter().apply {
            setOnClickListener {
                mainNavController.navigate(BottomNavFragmentDirections.toShopDetails(it))
            }
            setOnClickMapListener {
                requireContext().openMap(it.latitude, it.longitude)
            }
        }
        binding.errorState.retry.setOnClickListener {
            adapter.retry()
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect {
                binding.errorState.root.isVisible = it.refresh is LoadState.Error
                if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached) {
                    binding.emptyState.isVisible = adapter.itemCount < 1
                }
            }
        }
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.shops.collectLatest { adapter.submitData(it) }
        }
    }
}