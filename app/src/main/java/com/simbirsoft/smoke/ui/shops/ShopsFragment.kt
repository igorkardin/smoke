package com.simbirsoft.smoke.ui.shops

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentShopsBinding
import com.simbirsoft.smoke.domain.Shop
import com.simbirsoft.smoke.presentation.ShopViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import java.util.*
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
                openMap(it)
            }
        }
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.shops.collectLatest { adapter.submitData(it) }
        }
    }

    private fun openMap(shop: Shop) {
        val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", shop.latitude, shop.longitude)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        requireContext().startActivity(intent)
    }
}