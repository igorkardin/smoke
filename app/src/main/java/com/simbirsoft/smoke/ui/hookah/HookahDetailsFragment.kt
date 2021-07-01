package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.MainActivity
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahDetailBinding
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.presentation.ReviewViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HookahDetailsFragment : BaseFragment(R.layout.fragment_hookah_detail) {
    private val binding by viewBinding<FragmentHookahDetailBinding>()
    private val viewModel by viewModels<ReviewViewModel> { viewModelFactory }
    private val navArgs by navArgs<HookahDetailsFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ReviewViewModel.Factory

    override fun inject(app: App) {
        app.reviewComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navArgs.hookah.applyToView(binding)
        val adapter = ReviewAdapter()
        adapter.setOnClickListener { review ->
            // TODO
        }
        (requireActivity() as MainActivity?)?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.reviews.adapter = adapter
        binding.newReview.setOnClickListener {
            mainNavController.navigate(HookahDetailsFragmentDirections.toHookahReview(navArgs.hookah))
        }
        lifecycleScope.launchWhenStarted {
            viewModel.getReviews(navArgs.hookah).collectLatest { adapter.submitData(it) }
        }
        mainNavController.addOnDestinationChangedListener { _, _, _ ->
            lifecycleScope.launch {
                viewModel.reviews = null
                viewModel.getReviews(navArgs.hookah).collectLatest { adapter.submitData(it) }
            }
        }
    }
}

private fun Hookah.applyToView(binding: FragmentHookahDetailBinding) {
    Glide.with(binding.root).load(picture).into(binding.hookahImage)
    binding.description.text = description
    binding.hookahPrice.text = binding.root.context.getString(R.string.price, price)
    binding.rating.text = binding.root.context.getString(R.string.rating, rating.average)
    binding.title.text = name
}
