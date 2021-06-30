package com.simbirsoft.smoke.ui.video

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.App
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentShopsBinding
import com.simbirsoft.smoke.databinding.FragmentVideoBinding
import com.simbirsoft.smoke.presentation.ShopViewModel
import com.simbirsoft.smoke.presentation.VideoViewModel
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.main.BottomNavFragmentDirections
import com.simbirsoft.smoke.ui.openMap
import com.simbirsoft.smoke.ui.openVideo
import com.simbirsoft.smoke.ui.shops.ShopsAdapter
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class VideoFragment : BaseFragment(R.layout.fragment_video) {
    @Inject
    lateinit var viewModelFactory: VideoViewModel.Factory

    private val binding by viewBinding<FragmentVideoBinding>()
    private val viewModel by viewModels<VideoViewModel> { viewModelFactory }

    override fun inject(app: App) = app.videoComponent?.inject(this) ?: Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = VideoAdapter().apply {
            setOnClickListener {
                requireContext().openVideo(it.url)
            }
        }
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.videos.collectLatest { adapter.submitData(it) }
        }
    }
}