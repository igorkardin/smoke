package com.simbirsoft.smoke.ui.hookah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.databinding.FragmentRatingBinding
import com.simbirsoft.smoke.domain.Review
import com.simbirsoft.smoke.presentation.ReviewViewModel
import com.simbirsoft.smoke.ui.video.BaseSheetFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class HookahReviewFragment : BaseSheetFragment() {

    private lateinit var binding: FragmentRatingBinding

    private val viewModel by viewModels<ReviewViewModel> { viewModelFactory }
    private val navArgs by navArgs<HookahReviewFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ReviewViewModel.Factory

    override fun inject(app: App) {
        app.reviewComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            addReview()
            dismiss()
        }
        binding.buttonCancel.setOnClickListener { dismiss() }
    }

    private fun addReview() {
        val author = binding.name.text.toString()
        val body = binding.rateDesc.text.toString()
        val rating = binding.materialRatingBar.rating.toLong()
        val review = Review(hookahId = navArgs.hookah.id, author = author, body = body, rating = rating)
        lifecycleScope.launch {
            viewModel.addReview(review)
        }
    }
}