package com.simbirsoft.smoke.ui.shops

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.MainActivity
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentShopDetailBinding
import com.simbirsoft.smoke.presentation.DiscountViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.hookah.HookahDetailsFragmentArgs
import com.simbirsoft.smoke.ui.openMap
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopDetailsFragment : BaseFragment(R.layout.fragment_shop_detail) {
    @Inject
    lateinit var viewModelFactory: DiscountViewModel.Factory

    private val binding by viewBinding<FragmentShopDetailBinding>()
    private val viewModel by viewModels<DiscountViewModel> { viewModelFactory }

    private val navArgs by navArgs<ShopDetailsFragmentArgs>()

    override fun inject(app: App) = app.discountComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PromotionsAdapter()
        binding.promotions.adapter = adapter
        (requireActivity() as MainActivity?)?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        binding.map.setOnClickListener {
            requireContext().openMap(navArgs.shop.latitude, navArgs.shop.longitude)
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
        binding.title.text = navArgs.shop.name
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        lifecycleScope.launchWhenStarted {
            viewModel.promotions.collectLatest { adapter.submitData(it) }
        }
    }
}
