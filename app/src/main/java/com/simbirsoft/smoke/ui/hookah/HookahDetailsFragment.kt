package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentHookahDetailBinding
import com.simbirsoft.smoke.domain.Hookah
import com.simbirsoft.smoke.presentation.ReviewViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import javax.inject.Inject

class HookahDetailsFragment : BaseFragment(R.layout.fragment_hookah_detail) {
    private val binding by viewBinding<FragmentHookahDetailBinding>()
    val viewModel by viewModels<ReviewViewModel> { viewModelFactory }
    private val navArgs by navArgs<HookahDetailsFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ReviewViewModel.Factory

    override fun inject(app: App) {
        app.reviewComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navArgs.hookah.applyToView(binding)
        //TODO LOAD REVIEWS
    }
}

private fun Hookah.applyToView(binding: FragmentHookahDetailBinding) {
    Glide.with(binding.root).load(picture).into(binding.hookahImage)
    binding.description.text = description
    binding.hookahPrice.text = price.toString()
    binding.rating.text = rating.average.toString()
    binding.toolbar.title = name
}
